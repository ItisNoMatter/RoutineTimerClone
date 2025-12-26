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
                    timerState = TimerState(),
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

        fun onClickNext() {
            val state = uiState.value
            if (state.routine is LoadedValue.Done && uiState.value.currentTaskIndex < state.routine.value.tasks.size - 1) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTaskIndex = currentState.currentTaskIndex + 1,
                        timerState =
                            TimerState(
                                remainSeconds =
                                    (uiState.value.routine as LoadedValue.Done<Routine>).value
                                        .tasks[currentState.currentTaskIndex + 1]
                                        .duration.getTotalSeconds(),
                            ).start(),
                    )
                }
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
                                TimerState(
                                    remainSeconds =
                                        state.routine.value
                                            .tasks[currentState.currentTaskIndex - 1]
                                            .duration.getTotalSeconds(),
                                ),
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
