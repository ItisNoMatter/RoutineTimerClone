package jp.itIsNoMatter.routineTimerClone.ui.task.edit

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskEditUiState(
    val task: LoadedValue<Task>,
    val taskTitle: String = "",
    val taskDuration: Duration = Duration.Zero,
    val announceFlag: Boolean = true,
) {
    companion object {
        val InitialState =
            TaskEditUiState(
                task = LoadedValue.Loading,
            )
    }
}
