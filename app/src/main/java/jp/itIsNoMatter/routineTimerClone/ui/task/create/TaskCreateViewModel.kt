package jp.itIsNoMatter.routineTimerClone.ui.task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
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
class TaskCreateViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val parentRoutineId: Long = savedStateHandle.toRoute<Route.TaskCreate>().routineId
        private val _uiState: MutableStateFlow<TaskCreateUiState> =
            MutableStateFlow(
                TaskCreateUiState.InitialState,
            )
        val uiState: StateFlow<TaskCreateUiState> = _uiState.asStateFlow()
        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()

        fun create() {
            viewModelScope.launch {
                val taskId =
                    routineRepository.insertTask(
                        Task.Empty,
                        parentRoutineId,
                    )
                _uiState.update {
                    it.copy(
                        task =
                            LoadedValue.Done(
                                Task.Empty.copy(id = taskId),
                            ),
                    )
                }
            }
        }

        fun onClickBackButton() {
            viewModelScope.launch {
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }

        fun onTaskTitleChange(title: String) {
            _uiState.update { it.copy(taskTitle = title) }
            val state = uiState.value
            if (state.task !is LoadedValue.Done) return
            viewModelScope.launch {
                routineRepository.updateTask(
                    state.task.value.copy(name = title),
                    parentRoutineId,
                )
            }
        }

        fun onTaskDurationMinutesChange(
            task: Task,
            int: Int,
        ) {
            viewModelScope.launch {
                val duration = task.duration.copy(minutes = int)
                routineRepository.updateTask(
                    task.copy(duration = duration),
                    parentRoutineId,
                )
            }
        }

        fun onTaskDurationSecondsChange(
            task: Task,
            int: Int,
        ) {
            viewModelScope.launch {
                val duration = task.duration.copy(seconds = int)
                routineRepository.updateTask(
                    task.copy(duration = duration),
                    parentRoutineId,
                )
            }
        }

        fun onToggleAnnounceRemainingTime(task: Task) {
            viewModelScope.launch {
                routineRepository.updateTask(
                    task.copy(announceRemainingTimeFlag = !task.announceRemainingTimeFlag),
                    parentRoutineId,
                )
            }
        }

        fun openDurationInputDialog() {
            _uiState.update {
                it.copy(showDurationInputDialog = true)
            }
        }

        fun updateDuration(
            task: Task,
            duration: Duration,
        ) {
            viewModelScope.launch {
                routineRepository.updateTask(
                    task,
                    parentRoutineId,
                )
            }
        }

        fun closeDurationInputDialog() {
            _uiState.update {
                it.copy(showDurationInputDialog = false)
            }
        }
    }
