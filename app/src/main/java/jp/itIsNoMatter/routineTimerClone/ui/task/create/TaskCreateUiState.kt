package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskCreateUiState(
    val task: LoadedValue<Task>,
    val showDurationInputDialog: Boolean,
    val taskTitle: String = "",
) {
    companion object {
        val InitialState =
            TaskCreateUiState(
                task = LoadedValue.Loading,
                showDurationInputDialog = false,
            )
    }
}
