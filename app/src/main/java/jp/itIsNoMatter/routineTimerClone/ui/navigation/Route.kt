package jp.itIsNoMatter.routineTimerClone.ui.navigation

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

    @Serializable
    data class TaskCreate(
        val routineId: Long,
    ): Route

    @Serializable
    data class TaskEdit(
        val parentRoutineId: Long,
        val taskId: Long,
    ):Route
}
