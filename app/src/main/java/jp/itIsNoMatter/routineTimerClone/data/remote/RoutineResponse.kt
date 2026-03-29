package jp.itIsNoMatter.routineTimerClone.data.remote

import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.local.entity.TaskEntity
import kotlinx.serialization.Serializable

@Serializable
data class RoutineResponse(
    val id: String,
    val name: String,
    val tasks: List<TaskResponse>,
)

@Serializable
data class TaskResponse(
    val id: String,
    val name: String,
    val seconds: Int,
    val announceRemainingTimeFlag: Boolean,
    val orderIndex: Int,
)

fun RoutineResponse.toEntity(): RoutineEntity {
    return RoutineEntity(
        id = this.id,
        name = this.name,
    )
}

// タスクも同様に
fun TaskResponse.toEntity(routineId: String): TaskEntity {
    return TaskEntity(
        id = this.id,
        parentRoutineId = routineId,
        name = this.name,
        seconds = this.seconds,
        announceRemainingTimeFlag = this.announceRemainingTimeFlag,
        orderIndex = this.orderIndex,
    )
}
