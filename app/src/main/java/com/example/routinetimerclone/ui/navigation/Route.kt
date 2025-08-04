package com.example.routinetimerclone.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object RoutineList : Route

    @Serializable
    data class RoutineEdit(
        val routineId: Long,
    ) : Route

    @Serializable
    data object RoutineCreate : Route
}
