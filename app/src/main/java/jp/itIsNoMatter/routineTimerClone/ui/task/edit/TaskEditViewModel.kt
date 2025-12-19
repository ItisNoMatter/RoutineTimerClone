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
import jp.itIsNoMatter.routineTimerClone.domain.model.toMinutes
import jp.itIsNoMatter.routineTimerClone.domain.model.toSeconds
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
                                    taskTitle = value.name,
                                    taskDuration = value.duration,
                                    announceFlag = value.announceRemainingTimeFlag,
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

        fun onTaskDurationMinutesChange(minutes: Int) {
            val formatedMinutes = minutes.toMinutes()
            _uiState.update { it.copy(taskDuration = it.taskDuration.copy(minutes = formatedMinutes)) }
            val state = uiState.value
            if (state.task !is LoadedValue.Done) return
            viewModelScope.launch {
                routineRepository.updateTask(
                    state.task.value.copy(
                        duration = uiState.value.taskDuration.copy(minutes = formatedMinutes),
                    ),
                    parentRoutineId,
                )
            }
        }

        fun onTaskDurationSecondsChange(seconds: Int) {
            val formatedSeconds = seconds.toSeconds()
            _uiState.update { it.copy(taskDuration = it.taskDuration.copy(seconds = formatedSeconds)) }
            val state = uiState.value
            if (state.task !is LoadedValue.Done) return
            viewModelScope.launch {
                routineRepository.updateTask(
                    state.task.value.copy(
                        duration = uiState.value.taskDuration.copy(seconds = formatedSeconds),
                    ),
                    parentRoutineId,
                )
            }
        }

        fun onToggleAnnounceRemainingTime(checked: Boolean) {
            val state = uiState.value
            if (state.task !is LoadedValue.Done) return
            viewModelScope.launch {
                val currentTask =
                    state.task.value.copy(
                        announceRemainingTimeFlag = checked,
                    )
                routineRepository.updateTask(
                    currentTask,
                    parentRoutineId,
                )
                _uiState.update {
                    it.copy(
                        announceFlag = checked,
                        task = LoadedValue.Done(currentTask),
                    )
                }
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
    }
