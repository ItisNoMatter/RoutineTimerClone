package jp.itIsNoMatter.routineTimerClone.ui.routineCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.getOrNull
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import jp.itIsNoMatter.routineTimerClone.ui.navigation.Route
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineCreateViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<RoutineCreateUiState>(RoutineCreateUiState.Loading)
        val uiState: StateFlow<RoutineCreateUiState> = _uiState.asStateFlow()

        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()

        fun create() {
            if (uiState.value is RoutineCreateUiState.Done) return
            viewModelScope.launch {
                try {
                    val routineId = routineRepository.insertRoutine(Routine.Empty)

                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
                                RoutineCreateUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineCreateUiState.Error(e)
                    }
                }
            }
        }

        fun fetch(routineId: Long) {
            viewModelScope.launch {
                try {
                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
                                RoutineCreateUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineCreateUiState.Error(e)
                    }
                }
            }
        }

        fun onRoutineTitleChange(title: String) {
            val state = uiState.value
            if (state !is RoutineCreateUiState.Done) return
            viewModelScope.launch {
                routineRepository.updateRoutine(state.routine.copy(name = title))
            }
        }

        fun onClickAddTaskButton() {
            val state = uiState.value
            if (state !is RoutineCreateUiState.Done) return
            viewModelScope.launch {
                if (uiState.value is RoutineCreateUiState.Done) {
                    val routineId = state.routine.id
                    _navigateTo.emit(NavEvent.NavigateTo(route = Route.TaskCreate(routineId)))
                }
            }
        }

        fun onClickTaskCard(taskId: Long) {
            val state = uiState.value
            if (state !is RoutineCreateUiState.Done) return
            val parentRoutineId = state.routine.id
            viewModelScope.launch {
                _navigateTo.emit(NavEvent.NavigateTo(route = Route.TaskEdit(parentRoutineId, taskId)))
            }
        }

        fun onClickBackButton() {
            val state = uiState.value
            viewModelScope.launch {
                if (state is RoutineCreateUiState.Done && state.routine.name.isBlank()) {
                    routineRepository.deleteRoutineById(state.routine.id)
                }
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }
    }
