package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.domain.model.Task

data class TaskCreateUiActions(
    val onClickBackButton: () -> Unit,
    val onTaskNameChange: (String) -> Unit,
    val onTaskMinuteChange: (Task, Int) -> Unit,
    val onTaskSecondChange: (Task, Int) -> Unit,
    val onToggleAnnounceRemainingTimeFlag: (Task) -> Unit,
) {
    companion object {
        val Noop =
            TaskCreateUiActions(
                onClickBackButton = {},
                onTaskNameChange = {},
                onTaskMinuteChange = { _, _ -> },
                onTaskSecondChange = { _, _ -> },
                onToggleAnnounceRemainingTimeFlag = {},
            )
    }
}
