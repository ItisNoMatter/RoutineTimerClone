package com.example.routinetimerclone.data

import com.example.routinetimerclone.data.entitiy.TaskEntity
import com.example.routinetimerclone.data.entitiy.mapper.TaskModelMapper
import com.example.routinetimerclone.domain.model.Duration
import com.example.routinetimerclone.domain.model.Task
import org.junit.Test

class TaskModelMapperTest {
    @Test
    fun taskModelMapperTest() {
        val task = Task(1, "Task 1", Duration(1, 30))
        val taskEntity = TaskModelMapper.toEntity(task, 1)
        val task2 = TaskModelMapper.toDomain(taskEntity)
        assert(task == task2)
    }

    @Test
    fun taskModelMapper_toDomainTest() {
        val taskEntity = TaskEntity(1, "Task 1", 90, 1)
        val task = TaskModelMapper.toDomain(taskEntity)
        val expectedTask = Task(1, "Task 1", Duration(1, 30))
        assert(task == expectedTask)
    }

    @Test
    fun taskModelMapper_toEntityTest() {
        val task = Task(1, "Task 1", Duration(1, 30))
        val taskEntity = TaskModelMapper.toEntity(task, 1)
        val expectedTaskEntity = TaskEntity(1, "Task 1", 90, 1)
        assert(taskEntity == expectedTaskEntity)
    }
}
