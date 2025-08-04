package com.example.routineTimerClone.data.entitiy.mapper

import com.example.routineTimerClone.data.entitiy.RoutineEntity
import com.example.routineTimerClone.data.entitiy.RoutineWithTasks
import com.example.routineTimerClone.data.entitiy.TaskEntity
import com.example.routineTimerClone.domain.model.Routine

object RoutineModelMapper {
    fun toDomain(
        routineEntity: RoutineEntity,
        tasksEntities: List<TaskEntity>,
    ): Routine {
        return Routine(
            routineEntity.id,
            routineEntity.name,
            tasksEntities.map { TaskModelMapper.toDomain(it) },
        )
    }

    fun toEntity(routine: Routine): RoutineWithTasks {
        return RoutineWithTasks(
            RoutineEntity(routine.id, routine.name),
            routine.tasks.map { TaskModelMapper.toEntity(it, routine.id) },
        )
    }
}
