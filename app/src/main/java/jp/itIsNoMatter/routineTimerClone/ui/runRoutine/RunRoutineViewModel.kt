package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import android.app.Application
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.R
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.TimerState
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import jp.itIsNoMatter.routineTimerClone.ui.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunRoutineViewModel
    @Inject
    constructor(
        application: Application,
        private val routineRepository: RoutineRepository,
        savedStateHandle: SavedStateHandle,
    ) : AndroidViewModel(application) {
        private val routineId: Long = savedStateHandle.toRoute<Route.RunRoutine>().routineId

        private val _uiState: MutableStateFlow<RunRoutineUiState> =
            MutableStateFlow(
                RunRoutineUiState(
                    routine = LoadedValue.Loading,
                    timerState = TimerState.Unload,
                    currentTaskIndex = 0,
                ),
            )

        val uiState = _uiState.asStateFlow()

        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()

        private val  soundPool: SoundPool = SoundPool.Builder().setMaxStreams(1).build()
        private val soundId = soundPool.load(getApplication(), R.raw.task_finish, 1)

        init {
            viewModelScope.launch {
                routineRepository.getRoutine(routineId).collect { routine ->
                    _uiState.update { currentState ->
                        val newTimerState =
                            if (routine is LoadedValue.Done) {
                                val firstTask = routine.value.tasks.firstOrNull()
                                if (firstTask != null) {
                                    TimerState(
                                        isRunning = false,
                                        totalSeconds = firstTask.duration.getTotalSeconds(),
                                        remainSeconds = firstTask.duration.getTotalSeconds(),
                                        onTimeOver = { onClickNext() },
                                    )
                                } else {
                                    currentState.timerState
                                }
                            } else {
                                currentState.timerState
                            }
                        currentState.copy(
                            routine = routine,
                            timerState = newTimerState,
                        )
                    }
                }
            }
        }

        private fun onFinishLastTask() {
            _uiState.update { currentState ->
                currentState.copy(
                    finishedAllTasks = true,
                    timerState =
                        currentState.timerState.copy(
                            isRunning = false,
                        ),
                )
            }
        }

        private fun goToNextTask() {
            _uiState.update { currentState ->
                val nextTaskIndex = currentState.currentTaskIndex + 1
                val newState =
                    currentState.copy(
                        currentTaskIndex = nextTaskIndex,
                        timerState =
                            currentState.timerState.copy(
                                remainSeconds =
                                    (uiState.value.routine as LoadedValue.Done<Routine>).value
                                        .tasks[nextTaskIndex]
                                        .duration.getTotalSeconds(),
                            ),
                    )
                if (currentState.timerState.isRunning) {
                    newState.copy(
                        timerState = newState.timerState.start(),
                    )
                } else {
                    newState
                }
            }
        }

        fun onClickNext() {
            val state = uiState.value
            val isLastTask: Boolean =
                state.routine is LoadedValue.Done && uiState.value.currentTaskIndex == state.routine.value.tasks.size - 1
            val hasNextTask: Boolean =
                state.routine is LoadedValue.Done && uiState.value.currentTaskIndex < state.routine.value.tasks.size - 1

            soundPool.play(soundId,1f, 1f, 0, 0, 1f)

            if (isLastTask) {
                onFinishLastTask()
                return
            }

            if (hasNextTask) {
                goToNextTask()
            }
        }

        fun onClickPrevious() {
            val state = uiState.value
            if (state.routine is LoadedValue.Done) {
                if (state.currentTaskIndex > 0) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentTaskIndex = currentState.currentTaskIndex - 1,
                            timerState =
                                currentState.timerState.copy(
                                    remainSeconds =
                                        state.routine.value
                                            .tasks[currentState.currentTaskIndex - 1]
                                            .duration.getTotalSeconds(),
                                ),
                            finishedAllTasks = false,
                        )
                    }
                }
            }
        }

        fun onClickPlay() {
            _uiState.update { currentState ->
                currentState.copy(
                    timerState = currentState.timerState.start(),
                )
            }
            viewModelScope.launch {
                while (isActive && uiState.value.timerState.isRunning) {
                    delay(1000)
                    _uiState.update { currentState ->
                        currentState.copy(
                            timerState = currentState.timerState.tick(),
                        )
                    }
                }
            }
        }

        fun onClickPause() {
            _uiState.update { currentState ->
                currentState.copy(
                    timerState = currentState.timerState.pause(),
                )
            }
        }

        fun onClickBackButton() {
            viewModelScope.launch {
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }
    }
