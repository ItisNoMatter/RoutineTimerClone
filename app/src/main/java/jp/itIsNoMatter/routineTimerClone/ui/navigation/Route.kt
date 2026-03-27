package jp.itIsNoMatter.routineTimerClone.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object RoutineList : Route

    @Serializable
    data class RoutineEdit(
        val routineId: String,
    ) : Route

    @Serializable
    data object RoutineCreate : Route

    @Serializable
    data class TaskCreate(
        val routineId: String,
    ) : Route

    @Serializable
    data class TaskEdit(
        val parentRoutineId: String,
        val taskId: String,
    ) : Route

    @Serializable
    data class RunRoutine(
        val routineId: String,
    ) : Route
}
