package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.TimerState
import jp.itIsNoMatter.routineTimerClone.ui.navigation.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunRoutineViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
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

        init {
            viewModelScope.launch {
                routineRepository.getRoutine(routineId).collect { routine ->
                    _uiState.update { currentState ->
                        val newTimerState =
                            if (routine is LoadedValue.Done) {
                                val firstTask = routine.value.tasks.firstOrNull()
                                if (firstTask != null) {
                                    TimerState(
                                        isRunning = true,
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
                    finished = true,
                    timerState =
                        currentState.timerState.copy(
                            isRunning = false,
                        ),
                )
            }
        }

        private fun goToNextTask()  {
            _uiState.update { currentState ->
                val nextTaskIndex = currentState.currentTaskIndex + 1
                currentState.copy(
                    currentTaskIndex = nextTaskIndex,
                    timerState =
                        currentState.timerState.copy(
                            remainSeconds =
                                (uiState.value.routine as LoadedValue.Done<Routine>).value
                                    .tasks[nextTaskIndex]
                                    .duration.getTotalSeconds(),
                        ).start(),
                )
            }
        }

        fun onClickNext() {
            val state = uiState.value
            val isLastTask: Boolean =
                state.routine is LoadedValue.Done && uiState.value.currentTaskIndex == state.routine.value.tasks.size - 1
            val hasNextTask: Boolean =
                state.routine is LoadedValue.Done && uiState.value.currentTaskIndex < state.routine.value.tasks.size - 1

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
                            finished = false,
                        )
                    }
                }
            }
        }

        fun onClickPlay() {
            if (_uiState.value.timerState.isRunning) return

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
    }
