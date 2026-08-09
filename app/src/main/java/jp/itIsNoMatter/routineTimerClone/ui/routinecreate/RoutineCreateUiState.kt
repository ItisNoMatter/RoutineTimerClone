package jp.itIsNoMatter.routineTimerClone.ui.routinecreate

import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

sealed interface RoutineCreateUiState {
    data object Loading : RoutineCreateUiState

    data class Done(val routine: Routine, val isSaving: Boolean = false) : RoutineCreateUiState

    data class Error(val e: Exception) : RoutineCreateUiState
}
