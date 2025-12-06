package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskCreateUiState(
    val task: LoadedValue<Task>,
    val taskTitle: String = "",
    val taskDuration: Duration = Duration.Zero,
) {
    companion object {
        val InitialState =
            TaskCreateUiState(
                task = LoadedValue.Loading,
            )
    }
}
