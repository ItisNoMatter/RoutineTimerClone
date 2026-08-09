package jp.itIsNoMatter.routineTimerClone.ui.routineCreate

import android.util.Log
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
class RoutineCreateViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val routineRepository = mockk<RoutineRepository>()
    private lateinit var viewModel: RoutineCreateViewModel

    private val routine = Routine(id = "routine-1", name = "test", tasks = emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        coEvery { routineRepository.insertRoutine(any()) } just Runs
        every { routineRepository.getRoutine(any()) } returns flowOf(LoadedValue.Done(routine))
        viewModel = RoutineCreateViewModel(routineRepository)
        viewModel.create()
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `onClickBackButton - 保存処理が完了するまでisSavingがtrueになり、その間の再入力は無視されること`() =
        runTest(testDispatcher) {
            val navEvents = mutableListOf<NavEvent>()
            val collectJob = launch { viewModel.navigateTo.collect { navEvents.add(it) } }

            val updateRoutineGate = CompletableDeferred<Unit>()
            coEvery { routineRepository.updateRoutine(routine) } coAnswers { updateRoutineGate.await() }

            // 戻る操作 -> 保存処理が完了するまでisSaving=trueになり、画面遷移はまだ起きない
            viewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()

            val savingState = viewModel.uiState.value
            assertThat(savingState is RoutineCreateUiState.Done, `is`(true))
            assertThat((savingState as RoutineCreateUiState.Done).isSaving, `is`(true))
            assertThat(navEvents.isEmpty(), `is`(true))

            // 保存中に戻る操作をしても無視され、保存処理は1回しか実行されないこと
            viewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()
            coVerify(exactly = 1) { routineRepository.updateRoutine(routine) }

            // 保存処理が完了すると画面遷移(NavigateBack)が発火すること
            updateRoutineGate.complete(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(navEvents, `is`(listOf(NavEvent.NavigateBack)))

            collectJob.cancel()
        }

    @Test
    fun `onClickBackButton - タイトルが空の場合はdeleteRoutineByIdが呼ばれ、保存完了まで連打が無視されること`() =
        runTest(testDispatcher) {
            val blankRoutine = routine.copy(name = "")
            every { routineRepository.getRoutine(any()) } returns flowOf(LoadedValue.Done(blankRoutine))
            val freshViewModel = RoutineCreateViewModel(routineRepository)
            freshViewModel.create()
            testDispatcher.scheduler.advanceUntilIdle()

            val navEvents = mutableListOf<NavEvent>()
            val collectJob = launch { freshViewModel.navigateTo.collect { navEvents.add(it) } }

            val deleteRoutineGate = CompletableDeferred<Unit>()
            coEvery { routineRepository.deleteRoutineById(blankRoutine.id) } coAnswers { deleteRoutineGate.await() }

            freshViewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()

            val savingState = freshViewModel.uiState.value
            assertThat(savingState is RoutineCreateUiState.Done, `is`(true))
            assertThat((savingState as RoutineCreateUiState.Done).isSaving, `is`(true))

            freshViewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()
            coVerify(exactly = 1) { routineRepository.deleteRoutineById(blankRoutine.id) }

            deleteRoutineGate.complete(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(navEvents, `is`(listOf(NavEvent.NavigateBack)))

            collectJob.cancel()
        }

    @Test
    fun `create - 保存中にRoomの再emitが起きてもisSavingがtrueのまま保持されること`() =
        runTest(testDispatcher) {
            // RoomのgetRoutine()相当。updateRoutine()等のDB書き込みで再emitされうるホットなFlowを模倣する
            val routineFlow = MutableSharedFlow<LoadedValue<Routine>>(replay = 1)
            routineFlow.tryEmit(LoadedValue.Done(routine))
            every { routineRepository.getRoutine(any()) } returns routineFlow

            val freshViewModel = RoutineCreateViewModel(routineRepository)
            freshViewModel.create()
            testDispatcher.scheduler.advanceUntilIdle()

            val updateRoutineGate = CompletableDeferred<Unit>()
            coEvery { routineRepository.updateRoutine(routine) } coAnswers {
                // 保存の書き込みによってRoomの監視Flowが再emitされる状況を再現する
                routineFlow.emit(LoadedValue.Done(routine))
                updateRoutineGate.await()
            }

            freshViewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = freshViewModel.uiState.value
            assertThat(state is RoutineCreateUiState.Done, `is`(true))
            assertThat((state as RoutineCreateUiState.Done).isSaving, `is`(true))

            updateRoutineGate.complete(Unit)
            testDispatcher.scheduler.advanceUntilIdle()
        }

    @Test
    fun `onClickBackButton - 保存処理が失敗しても画面遷移(NavigateBack)が発火すること`() =
        runTest(testDispatcher) {
            val navEvents = mutableListOf<NavEvent>()
            val collectJob = launch { viewModel.navigateTo.collect { navEvents.add(it) } }

            coEvery { routineRepository.updateRoutine(routine) } throws RuntimeException("network error")

            viewModel.onClickBackButton()
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(navEvents, `is`(listOf(NavEvent.NavigateBack)))

            collectJob.cancel()
        }
}
