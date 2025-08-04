package com.example.routinetimerclone.ui.routineCreate

import com.example.routinetimerclone.domain.model.Routine

sealed interface RoutineCreateUiState {
    data object Loading : RoutineCreateUiState

    data class Done(val routine: Routine) : RoutineCreateUiState

    data class Error(val e: Exception) : RoutineCreateUiState
}
