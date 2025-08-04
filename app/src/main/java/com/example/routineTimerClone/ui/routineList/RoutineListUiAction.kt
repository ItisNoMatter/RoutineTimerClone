package com.example.routineTimerClone.ui.routineList

import com.example.routineTimerClone.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class RoutineListUiAction(
    val onPlayButtonClick: (routineId: Long) -> Unit,
    val onAddButtonClick: () -> Unit,
    val getAllRoutines: () -> Flow<List<Routine>>,
) {
    companion object {
        val Noop =
            RoutineListUiAction(
                onPlayButtonClick = {},
                onAddButtonClick = {},
                getAllRoutines = { emptyFlow() },
            )
    }
}
