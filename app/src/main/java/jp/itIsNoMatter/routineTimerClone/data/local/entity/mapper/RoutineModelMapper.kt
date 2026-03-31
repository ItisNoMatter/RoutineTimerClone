package jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper

import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.local.entity.TaskEntity
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

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

    fun toResponse(routine: Routine): RoutineResponse {
        return RoutineResponse(
            id = routine.id,
            name = routine.name,
            tasks = routine.tasks.map { TaskModelMapper.toResponse(it) },
        )
    }
}
