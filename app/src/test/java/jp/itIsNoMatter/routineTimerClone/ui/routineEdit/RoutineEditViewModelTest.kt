package jp.itIsNoMatter.routineTimerClone.ui.routineEdit

import android.util.Log
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoutineEditViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val routineRepository = mockk<RoutineRepository>()
    private lateinit var viewModel: RoutineEditViewModel

    private val routine =
        Routine(
            id = "routine-1",
            name = "test",
            tasks =
                listOf(
                    Task(id = "task-1", name = "task", duration = Duration(1, 0), announceRemainingTimeFlag = true),
                ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { routineRepository.getRoutine(routine.id) } returns flowOf(LoadedValue.Done(routine))
        viewModel = RoutineEditViewModel(routineRepository)
        viewModel.fetch(routine.id)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackScreen - 保存処理が完了するまでisSavingがtrueになり、その間の再入力は無視されること`() =
        runTest(testDispatcher) {
            val navEvents = mutableListOf<NavEvent>()
            val collectJob = launch { viewModel.navigateTo.collect { navEvents.add(it) } }

            val updateRoutineGate = CompletableDeferred<Unit>()
            coEvery { routineRepository.updateRoutine(routine) } coAnswers { updateRoutineGate.await() }

            // 戻る操作 -> 保存処理が完了するまでisSaving=trueになり、画面遷移はまだ起きない
            viewModel.onBackScreen()
            testDispatcher.scheduler.advanceUntilIdle()

            val savingState = viewModel.uiState.value
            assertThat(savingState is RoutineEditUiState.Done, `is`(true))
            assertThat((savingState as RoutineEditUiState.Done).isSaving, `is`(true))
            assertThat(navEvents.isEmpty(), `is`(true))

            // 保存中に戻る操作をしても無視され、保存処理は1回しか実行されないこと
            viewModel.onBackScreen()
            testDispatcher.scheduler.advanceUntilIdle()
            coVerify(exactly = 1) { routineRepository.updateRoutine(routine) }

            // 保存処理が完了すると画面遷移(NavigateBack)が発火すること
            updateRoutineGate.complete(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(navEvents, `is`(listOf(NavEvent.NavigateBack)))

            collectJob.cancel()
        }

    @Test
    fun `onBackScreen - 保存処理が失敗しても画面遷移(NavigateBack)が発火すること`() =
        runTest(testDispatcher) {
            val navEvents = mutableListOf<NavEvent>()
            val collectJob = launch { viewModel.navigateTo.collect { navEvents.add(it) } }

            coEvery { routineRepository.updateRoutine(routine) } throws RuntimeException("network error")

            viewModel.onBackScreen()
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(navEvents, `is`(listOf(NavEvent.NavigateBack)))

            collectJob.cancel()
        }
}
