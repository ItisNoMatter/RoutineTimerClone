package jp.itIsNoMatter.routineTimerClone.ui.task.edit

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val args = savedStateHandle.toRoute<Route.TaskEdit>()
        val parentRoutineId: Long = args.parentRoutineId
        private val taskId: Long = args.taskId
        private val _uiState = MutableStateFlow(TaskEditUiState.InitialState)
        val uiState: StateFlow<TaskEditUiState> = _uiState.asStateFlow()
        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()
        private var fetchJob: Job? = null

        init {
            fetch()
        }

        fun fetch() {
            fetchJob?.cancel()
            fetchJob =
                viewModelScope.launch {
                    routineRepository.getTaskByTaskId(taskId).collect { value ->
                        if (value != null) {
                            _uiState.update {
                                it.copy(
                                    task = LoadedValue.Done(value),
                                )
                            }
                        }
                    }
                }
        }

        fun onClickBackButton() {
            viewModelScope.launch {
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }

        fun onTaskTitleChange(
            task: Task,
            title: String,
        ) {
            viewModelScope.launch {
                routineRepository.updateTask(
                    task.copy(name = title),
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

        fun openDurationInput() {
            _uiState.update {
                it.copy(showDurationInput = true)
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

        fun closeDurationInput() {
            _uiState.update {
                it.copy(showDurationInput = false)
            }
        }
    }
