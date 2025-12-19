package jp.itIsNoMatter.routineTimerClone.data

import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import org.junit.Test

class TaskModelMapperTest {
    val taskModelMapper = TaskModelMapper

    @Test
    fun taskModelMapperTest() {
        val task = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        val taskEntity = taskModelMapper.toEntity(task, 1)
        val task2 = taskModelMapper.toDomain(taskEntity)
        assert(task == task2)
    }

    @Test
    fun taskModelMapper_toDomainTest() {
        val taskEntity = TaskEntity(1, "Task 1", 90, 1)
        val task = taskModelMapper.toDomain(taskEntity)
        val expectedTask = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        assert(task == expectedTask)
    }

    @Test
    fun taskModelMapper_toEntityTest() {
        val task = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        val taskEntity = taskModelMapper.toEntity(task, 1)
        val expectedTaskEntity = TaskEntity(1, "Task 1", 90, 1)
        assert(taskEntity == expectedTaskEntity)
    }
}
