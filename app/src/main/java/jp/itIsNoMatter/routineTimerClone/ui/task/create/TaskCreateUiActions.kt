package jp.itIsNoMatter.routineTimerClone.ui.task.create

data class TaskCreateUiActions(
    val onClickBackButton: () -> Unit,
    val onTaskNameChange: (String) -> Unit,
    val onTaskMinuteChange: (Int) -> Unit,
    val onTaskSecondChange: (Int) -> Unit,
    val onToggleAnnounceRemainingTimeFlag: (Boolean) -> Unit,
) {
    companion object {
        val Noop =
            TaskCreateUiActions(
                onClickBackButton = {},
                onTaskNameChange = {},
                onTaskMinuteChange = {},
                onTaskSecondChange = {},
                onToggleAnnounceRemainingTimeFlag = {},
            )
    }
}
