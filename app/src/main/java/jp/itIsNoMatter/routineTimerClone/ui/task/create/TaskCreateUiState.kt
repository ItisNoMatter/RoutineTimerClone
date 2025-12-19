package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskCreateUiState(
    val task: LoadedValue<Task>,
    val taskTitle: String = "",
    val taskDuration: Duration = Duration.Zero,
    val announceFlag: Boolean = true,
) {
    companion object {
        val InitialState =
            TaskCreateUiState(
                task = LoadedValue.Loading,
            )
    }
}

interface TaskEditUiState {
    val task: LoadedValue<Task>
    val taskTitle: String
    val taskDuration: Duration
    val announceFlag: Boolean
}
