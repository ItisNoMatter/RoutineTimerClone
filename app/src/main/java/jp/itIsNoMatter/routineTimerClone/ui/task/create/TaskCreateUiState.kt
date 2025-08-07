package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskCreateUiState(
    val task: LoadedValue<Task>,
    val showDurationInput: Boolean,
) {
    companion object {
        val InitialState =
            TaskCreateUiState(
                task = LoadedValue.Loading,
                showDurationInput = false,
            )
    }
}
