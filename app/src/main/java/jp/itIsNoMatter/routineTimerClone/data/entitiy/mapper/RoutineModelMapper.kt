package jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper

import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
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
}
