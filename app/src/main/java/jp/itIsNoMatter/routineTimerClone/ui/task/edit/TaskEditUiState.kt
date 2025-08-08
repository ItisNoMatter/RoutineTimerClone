package jp.itIsNoMatter.routineTimerClone.ui.task.edit

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskEditUiState(
    val task: LoadedValue<Task>,
    val showDurationInput: Boolean,
) {
    companion object {
        val InitialState =
            TaskEditUiState(
                task = LoadedValue.Loading,
                showDurationInput = false,
            )
    }
}
