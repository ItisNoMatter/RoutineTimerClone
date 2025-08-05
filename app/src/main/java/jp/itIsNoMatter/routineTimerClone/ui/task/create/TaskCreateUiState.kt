package jp.itIsNoMatter.routineTimerClone.ui.task.create

import jp.itIsNoMatter.routineTimerClone.domain.model.Task

interface TaskCreateUiState {
    data object Loading : TaskCreateUiState

    data class Done(val task: Task) : TaskCreateUiState

    data class Error(val e: Exception) : TaskCreateUiState
}
