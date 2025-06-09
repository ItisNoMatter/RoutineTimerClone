package com.example.routinetimerclone.ui.routineEdit

import com.example.routinetimerclone.domain.model.Routine

sealed interface RoutineEditUiState {
    data object Loading : RoutineEditUiState

    data class Done(val routine: Routine) : RoutineEditUiState

    data class Error(val e: Exception) : RoutineEditUiState

    data object New : RoutineEditUiState
}
