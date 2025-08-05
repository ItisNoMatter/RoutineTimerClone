package jp.itIsNoMatter.routineTimerClone.ui.task.edit

import jp.itIsNoMatter.routineTimerClone.domain.model.Task

sealed interface TaskEditUiState {
    data object Loading : TaskEditUiState

    data class Done(val task: Task) : TaskEditUiState

    data class Error(val e: Exception) : TaskEditUiState
}
