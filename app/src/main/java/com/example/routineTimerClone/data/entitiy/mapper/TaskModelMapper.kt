package com.example.routineTimerClone.data.entitiy.mapper

import com.example.routineTimerClone.data.entitiy.TaskEntity
import com.example.routineTimerClone.domain.model.Duration
import com.example.routineTimerClone.domain.model.Task

object TaskModelMapper {
    fun toDomain(taskEntity: TaskEntity): Task {
        return Task(taskEntity.id, taskEntity.name, Duration.fromSeconds(taskEntity.seconds))
    }

    fun toEntity(
        task: Task,
        parentRoutineId: Long,
    ): TaskEntity {
        return TaskEntity(task.id, task.name, task.duration.getTotalSeconds(), parentRoutineId)
    }
}
