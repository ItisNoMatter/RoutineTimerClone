package com.example.routineTimerClone.ui.routineEdit

import com.example.routineTimerClone.domain.model.Routine

sealed interface RoutineEditUiState {
    data object Loading : RoutineEditUiState

    data class Done(val routine: Routine) : RoutineEditUiState

    data class Error(val e: Exception) : RoutineEditUiState
}
