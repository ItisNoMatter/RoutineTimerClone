package jp.itIsNoMatter.routineTimerClone.ui.routineedit

import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

sealed interface RoutineEditUiState {
    data object Loading : RoutineEditUiState

    data class Done(val routine: Routine, val isSaving: Boolean = false) : RoutineEditUiState

    data class Error(val e: Exception) : RoutineEditUiState
}
