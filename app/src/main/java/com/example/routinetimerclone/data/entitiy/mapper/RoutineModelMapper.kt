package com.example.routinetimerclone.data.entitiy.mapper

import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.TaskEntity
import com.example.routinetimerclone.domain.model.Routine

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
