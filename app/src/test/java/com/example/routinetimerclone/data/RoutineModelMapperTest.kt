package com.example.routinetimerclone.data

import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.TaskEntity
import com.example.routinetimerclone.data.entitiy.mapper.RoutineModelMapper
import com.example.routinetimerclone.domain.model.Duration
import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import org.junit.Test

class RoutineModelMapperTest {
    @Test
    fun routineModelMapperTest() {
        val task1 = Task(1, "Task 1", Duration(1, 30))
        val task2 = Task(2, "Task 2", Duration(2, 0))
        val routine = Routine(1, "Test Routine", listOf(task1, task2))

        val routineWithTasks = RoutineModelMapper.toEntity(routine)
        val routine2 = RoutineModelMapper.toDomain(routineWithTasks.routine, routineWithTasks.tasks)

        assert(routine == routine2)
    }

    @Test
    fun routineModelMapper_toDomainTest() {
        val taskEntity1 = TaskEntity(1, "Task 1", 90, 1)
        val taskEntity2 = TaskEntity(2, "Task 2", 120, 1)
        val routineEntity = RoutineEntity(1, "Test Routine")

        val expectedRoutine = Routine(1, "Test Routine", listOf(Task(1, "Task 1", Duration(1, 30)), Task(2, "Task 2", Duration(2, 0))))
        val routine = RoutineModelMapper.toDomain(routineEntity, listOf(taskEntity1, taskEntity2))

        assert(routine == expectedRoutine)
    }

    @Test
    fun routineModelMapper_toEntityTest() {
        val task1 = Task(1, "Task 1", Duration(1, 30))
        val task2 = Task(2, "Task 2", Duration(2, 0))
        val routine = Routine(1, "Test Routine", listOf(task1, task2))

        val routineWithTasks = RoutineModelMapper.toEntity(routine)
        val expectedTasksEntities = listOf(TaskEntity(1, "Task 1", 90, 1), TaskEntity(2, "Task 2", 120, 1))
        val expectedRoutineEntity = RoutineEntity(1, "Test Routine")

        assert(routineWithTasks.tasks == expectedTasksEntities)
        assert(routineWithTasks.routine == expectedRoutineEntity)
    }
}
