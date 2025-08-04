package jp.itIsNoMatter.routineTimerClone.ui.routineCreate

import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

sealed interface RoutineCreateUiState {
    data object Loading : RoutineCreateUiState

    data class Done(val routine: Routine) : RoutineCreateUiState

    data class Error(val e: Exception) : RoutineCreateUiState
}
