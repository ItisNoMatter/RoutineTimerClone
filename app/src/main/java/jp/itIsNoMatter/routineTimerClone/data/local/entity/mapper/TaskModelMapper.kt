package jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper

import jp.itIsNoMatter.routineTimerClone.data.local.entity.TaskEntity
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

object TaskModelMapper {
    fun toDomain(taskEntity: TaskEntity): Task {
        return Task(
            id = taskEntity.id,
            name = taskEntity.name,
            duration = Duration.fromSeconds(taskEntity.seconds),
            announceRemainingTimeFlag = taskEntity.announceRemainingTimeFlag,
        )
    }

    fun toEntity(
        task: Task,
        parentRoutineId: String,
    ): TaskEntity {
        return TaskEntity(
            id = task.id,
            name = task.name,
            seconds = task.duration.getTotalSeconds(),
            parentRoutineId = parentRoutineId,
            announceRemainingTimeFlag = task.announceRemainingTimeFlag,
        )
    }
}
