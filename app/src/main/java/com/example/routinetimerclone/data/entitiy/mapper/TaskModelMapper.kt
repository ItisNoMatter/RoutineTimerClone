package com.example.routinetimerclone.data.entitiy.mapper

import com.example.routinetimerclone.data.entitiy.TaskEntity
import com.example.routinetimerclone.domain.model.Duration
import com.example.routinetimerclone.domain.model.Task

class TaskModelMapper {
    companion object {
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
}
