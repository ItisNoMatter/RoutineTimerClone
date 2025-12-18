package jp.itIsNoMatter.routineTimerClone.ui.task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import jp.itIsNoMatter.routineTimerClone.domain.model.toMinutes
import jp.itIsNoMatter.routineTimerClone.domain.model.toSeconds
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
                routineRepository.getTaskByTaskId(taskId).collect { task ->
                    if (task != null) {
                        _uiState.update {
                            it.copy(
                                task = LoadedValue.Done(value = task),
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
            // まだタスクのロードが完了していなければ何もしない
            if (state.task !is LoadedValue.Done) return

            viewModelScope.launch {
                // 現在の「編集中の値」を使って保存用タスクを作る
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
    }
