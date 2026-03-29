package jp.itIsNoMatter.routineTimerClone.data.entity.mapper

import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entity.TaskEntity
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
