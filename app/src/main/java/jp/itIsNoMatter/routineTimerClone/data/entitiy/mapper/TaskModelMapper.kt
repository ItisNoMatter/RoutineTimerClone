package jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper

import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
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
        parentRoutineId: Long,
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
