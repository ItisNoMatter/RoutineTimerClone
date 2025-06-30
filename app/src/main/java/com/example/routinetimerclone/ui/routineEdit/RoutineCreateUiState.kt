package com.example.routinetimerclone.ui.routineEdit

import com.example.routinetimerclone.domain.model.Routine

sealed interface RoutineCreateUiState {
    data object Loading : RoutineCreateUiState

    data class Done(val routine: Routine) : RoutineCreateUiState

    data class Error(val e: Exception) : RoutineCreateUiState
}
