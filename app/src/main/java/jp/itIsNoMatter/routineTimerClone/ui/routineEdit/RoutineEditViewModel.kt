package jp.itIsNoMatter.routineTimerClone.ui.routineEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.getOrNull
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
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
class RoutineEditViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<RoutineEditUiState>(RoutineEditUiState.Loading)
        val uiState: StateFlow<RoutineEditUiState> = _uiState.asStateFlow()
        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()

        fun fetch(routineId: Long) {
            viewModelScope.launch {
                try {
                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
                                RoutineEditUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineEditUiState.Error(e)
                    }
                }
            }
        }

        fun onRoutineTitleChange(title: String) {
            val state = uiState.value
            if (state !is RoutineEditUiState.Done) return
            viewModelScope.launch {
                routineRepository.updateRoutine(state.routine.copy(name = title))
            }
        }

        fun onClickAddTaskButton() {
            viewModelScope.launch {
                if (uiState.value is RoutineEditUiState.Done) {
                    val routineId = (uiState.value as RoutineEditUiState.Done).routine.id
                    _navigateTo.emit(NavEvent.NavigateTo(route = Route.TaskCreate(routineId)))
                }
            }
        }

        fun onClickTaskCard(taskId: Long) {
            viewModelScope.launch {
                val parentRoutineId = (uiState.value as RoutineEditUiState.Done).routine.id
                _navigateTo.emit(NavEvent.NavigateTo(route = Route.TaskEdit(parentRoutineId, taskId)))
            }
        }

        fun onBackScreen() {
            viewModelScope.launch {
                deleteInvalidTasks()
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }

        private suspend fun deleteInvalidTasks() {
            val state = uiState.value
            if (state !is RoutineEditUiState.Done) return
            state.routine.tasks.forEach { task ->
                if (task.isInvalidValue) {
                    routineRepository.deleteTaskById(task.id)
                }
            }
        }
    }
