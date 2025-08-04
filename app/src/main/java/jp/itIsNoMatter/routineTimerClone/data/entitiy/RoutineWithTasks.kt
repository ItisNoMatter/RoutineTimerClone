package jp.itIsNoMatter.routineTimerClone.data.entitiy

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithTasks(
    @Embedded val routine: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentRoutineId",
    )
    val tasks: List<TaskEntity>,
)
