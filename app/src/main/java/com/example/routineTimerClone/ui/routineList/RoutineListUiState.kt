package com.example.routineTimerClone.ui.routineList

import com.example.routineTimerClone.domain.model.Routine

data class RoutineListUiState(
    val routines: List<Routine>,
) {
    companion object {
        val Dummy =
            RoutineListUiState(
                listOf(
                    Routine(1, "Routine 1", emptyList()),
                    Routine(2, "Routine 2", emptyList()),
                    Routine(3, "Routine 3", emptyList()),
                ),
            )
    }
}
