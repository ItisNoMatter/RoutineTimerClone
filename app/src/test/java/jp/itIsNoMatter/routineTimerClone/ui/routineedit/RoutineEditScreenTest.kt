package jp.itIsNoMatter.routineTimerClone.ui.routineedit

import android.util.Log
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import jp.itIsNoMatter.routineTimerClone.ui.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class RoutineEditScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = StandardTestDispatcher()
    private val routineRepository = mockk<RoutineRepository>()
    private val navHostController = mockk<NavHostController>(relaxed = true)

    private val routine =
        Routine(
            id = "routine-1",
            name = "test",
            tasks =
                listOf(
                    Task(id = "task-1", name = "task A", duration = Duration(1, 0), announceRemainingTimeFlag = true),
                ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    private fun setContent(routineToLoad: Routine): RoutineEditViewModel {
        every { routineRepository.getRoutine(routineToLoad.id) } returns flowOf(LoadedValue.Done(routineToLoad))
        val viewModel = RoutineEditViewModel(routineRepository)
        composeTestRule.setContent {
            RoutineEditScreen(routineId = routineToLoad.id, navHostController = navHostController, viewModel = viewModel)
        }
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()
        return viewModel
    }

    @Test
    fun `タイトルが空のとき戻るボタンが無効化され、タップしても保存・画面遷移が起きないこと`() {
        setContent(routine.copy(name = ""))

        composeTestRule.onNodeWithContentDescription("Back").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        coVerify(exactly = 0) { routineRepository.updateRoutine(any()) }
        verify(exactly = 0) { navHostController.popBackStack() }
    }

    @Test
    fun `タスク追加ボタンタップでタスク作成画面への遷移が呼ばれること`() {
        setContent(routine)

        composeTestRule.onNodeWithContentDescription("Add Task").performClick()
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        verify { navHostController.navigate(Route.TaskCreate(routine.id)) }
    }

    @Test
    fun `タスクカードタップでタスク編集画面への遷移が呼ばれること`() {
        setContent(routine)

        composeTestRule.onNodeWithText(routine.tasks[0].name).performClick()
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        verify { navHostController.navigate(Route.TaskEdit(routine.id, routine.tasks[0].id)) }
    }

    @Test
    fun `戻るボタンタップでupdateRoutineが呼ばれpopBackStackされること`() {
        coEvery { routineRepository.updateRoutine(routine) } just Runs

        setContent(routine)

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        coVerify { routineRepository.updateRoutine(routine) }
        verify { navHostController.popBackStack() }
    }

    @Test
    fun `エラー状態のときErrorのTextが表示されること`() {
        every { routineRepository.getRoutine(routine.id) } returns flow { throw RuntimeException("network error") }

        val viewModel = RoutineEditViewModel(routineRepository)
        composeTestRule.setContent {
            RoutineEditScreen(routineId = routine.id, navHostController = navHostController, viewModel = viewModel)
        }
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Error").assertExists()
    }
}
