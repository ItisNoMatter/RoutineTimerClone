package jp.itIsNoMatter.routineTimerClone.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineLocalDataSource
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepositoryImpl
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoutineRepositroryTest {
    private lateinit var routineRepository: RoutineRepository
    private lateinit var routineModelMapper: RoutineModelMapper
    private lateinit var taskModelMapper: TaskModelMapper
    private lateinit var routineLocalDataSource: RoutineLocalDataSource

    private val task1 = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
    private val task2 = Task(id = 2, name = "Task 2", minutes = 2, seconds = 0, announceRemainingTimeFlag = true)
    private val task3 = Task(id = 3, name = "Task 3", minutes = 3, seconds = 30, announceRemainingTimeFlag = true)
    private val task4 = Task(id = 4, name = "Task 4", minutes = 4, seconds = 0, announceRemainingTimeFlag = true)
    private val tasks1 = listOf(task1, task2)
    private val tasks2 = listOf(task3, task4)

    private val routine1 = Routine(1, "Routine 1", tasks1)
    private val routineEntity1 = RoutineEntity(1, "Routine 1")

    private val routine2 = Routine(2, "Routine 2", tasks2)
    private val routineEntity2 = RoutineEntity(2, "Routine 2")

    private val taskEntity1 = TaskEntity(1, "Task 1", 90, 1)
    private val taskEntity2 = TaskEntity(2, "Task 2", 150, 1)
    private val taskEntity3 = TaskEntity(3, "Task 3", 210, 2)
    private val taskEntity4 = TaskEntity(4, "Task 4", 270, 2)
    private val tasksEntity1 = listOf(taskEntity1, taskEntity2)
    private val tasksEntity2 = listOf(taskEntity3, taskEntity4)

    private val routineWithTasks1 = RoutineWithTasks(routine = routineEntity1, tasks = tasksEntity1)
    private val routineWithTasks2 = RoutineWithTasks(routine = routineEntity2, tasks = tasksEntity2)

    @Before
    fun setup() {
        routineModelMapper = mockk()
        taskModelMapper = mockk()
        routineLocalDataSource = mockk()

        routineRepository =
            RoutineRepositoryImpl(
                routineLocalDataSource,
                routineModelMapper,
                taskModelMapper,
            )

        every { routineModelMapper.toEntity(routine1) } returns routineWithTasks1
        every { routineModelMapper.toEntity(routine2) } returns routineWithTasks2
        every { routineModelMapper.toDomain(routineEntity1, tasksEntity1) } returns routine1
        every { routineModelMapper.toDomain(routineEntity2, tasksEntity2) } returns routine2
        every { taskModelMapper.toEntity(task1, 1) } returns taskEntity1
        every { taskModelMapper.toEntity(task2, 1) } returns taskEntity2
        every { taskModelMapper.toEntity(task3, 2) } returns taskEntity3
        every { taskModelMapper.toEntity(task4, 2) } returns taskEntity4
        every { taskModelMapper.toDomain(taskEntity1) } returns task1
        every { taskModelMapper.toDomain(taskEntity2) } returns task2
        every { taskModelMapper.toDomain(taskEntity3) } returns task3
        every { taskModelMapper.toDomain(taskEntity4) } returns task4

        coEvery { routineLocalDataSource.insertRoutineWithTasks(routineEntity1, tasksEntity1) } returns 1L
        coEvery { routineLocalDataSource.insertRoutineWithTasks(routineEntity2, tasksEntity2) } returns 2L

        every {
            routineLocalDataSource.getAllRoutines()
        } returns flowOf(listOf(routineWithTasks1, routineWithTasks2))
    }

    @Test
    fun `getAllRoutines should call routineLocalDataSource getAllRoutines`() {
        val mockResponse = listOf<List<RoutineWithTasks>>().asFlow()
        every { routineLocalDataSource.getAllRoutines() } returns mockResponse
        routineRepository.getAllRoutines()
        verify { routineLocalDataSource.getAllRoutines() }
    }

    @Test
    fun `getAllRoutines should return a list of routines`() =
        runBlocking {
            val result = routineRepository.getAllRoutines().first()
            val expected = listOf(routine1, routine2)
            assertEquals(expected, result)
        }

    @Test
    fun `getRoutine should call routineLocalDataSource getRoutineById`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getRoutineById(1) } returns flowOf(routineWithTasks1)
            routineRepository.getRoutine(id)
            verify { routineLocalDataSource.getRoutineById(id) }
        }

    @Test
    fun `getRoutine should return a routine`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getRoutineById(id) } returns flowOf(routineWithTasks1)
            val result = routineRepository.getRoutine(id).first()
            val expected = flowOf(LoadedValue.Done(routine1)).first()
            assertEquals(expected, result)
        }

    @Test
    fun `getRoutineByName should call routineLocalDataSource getRoutineByName`() =
        runBlocking {
            val name = "Routine 1"
            every { routineLocalDataSource.getRoutineByName(name) } returns flowOf(routineWithTasks1)
            routineRepository.getRoutineByName(name)
            verify { routineLocalDataSource.getRoutineByName(name) }
        }

    @Test
    fun `insertRoutine should call routineLocalDataSource insertRoutineWithTasks`() =
        runBlocking {
            routineRepository.insertRoutine(routine1)

            coVerify { routineLocalDataSource.insertRoutineWithTasks(routineEntity1, tasksEntity1) }
        }

    @Test
    fun `insertRoutine should return the inserted routine id`() =
        runBlocking {
            coEvery { routineLocalDataSource.insertRoutineWithTasks(routineEntity1, tasksEntity1) } returns 1L
            val result = routineRepository.insertRoutine(routine1)
            assertEquals(1L, result)
        }

    @Test
    fun `insert Routines should call routineLocalDataSource insertRoutineWithTasks`() =
        runBlocking {
            routineRepository.insertRoutines(listOf(routine1, routine2))
            coVerify { routineLocalDataSource.insertRoutineWithTasks(routineEntity1, tasksEntity1) }
            coVerify { routineLocalDataSource.insertRoutineWithTasks(routineEntity2, tasksEntity2) }
        }

    @Test
    fun `insert Routines should return the inserted routine ids`() =
        runBlocking {
            val result = routineRepository.insertRoutines(listOf(routine1, routine2))
            val expected = listOf(1L, 2L)
            assertEquals(expected, result)
        }

    @Test
    fun `updateRoutine should call routineLocalDataSource updateRoutineWithTasks`() =
        runBlocking {
            val newRoutine = Routine(1, "New Routine", tasks1)
            val newRoutineEntity = RoutineEntity(1, "New Routine")
            coEvery {
                routineLocalDataSource.updateRoutineWithTasks(newRoutineEntity, tasksEntity1)
            } returns Unit
            coEvery {
                routineModelMapper.toEntity(newRoutine)
            } returns RoutineWithTasks(newRoutineEntity, tasksEntity1)
            routineRepository.updateRoutine(newRoutine)
            coVerify {
                routineLocalDataSource.updateRoutineWithTasks(
                    newRoutineEntity,
                    tasksEntity1,
                )
            }
        }

    @Test
    fun `deleteRoutineById should call routineLocalDataSource deleteRoutineById`() =
        runBlocking {
            val id = 1L
            coEvery { routineLocalDataSource.deleteRoutineById(id) } returns Unit
            routineRepository.deleteRoutineById(id)
            coVerify { routineLocalDataSource.deleteRoutineById(id) }
        }

    @Test
    fun `getTasksByRoutineId should call routineLocalDataSource getTasksByRoutineId`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getTasksByRoutineId(id) } returns flowOf(tasksEntity1)
            routineRepository.getTasksByRoutineId(id)
            verify { routineLocalDataSource.getTasksByRoutineId(id) }
        }

    @Test
    fun `getTasksByRoutineId should return a list of tasks`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getTasksByRoutineId(id) } returns flowOf(tasksEntity1)
            val result = routineRepository.getTasksByRoutineId(id).first()
            val expected = tasks1
            assertEquals(result, expected)
        }

    @Test
    fun `getTaskById should return a task`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getTaskByTaskId(id) } returns flowOf(taskEntity1)
            val result = routineRepository.getTaskByTaskId(id).first()
            val expected = task1
            assertEquals(result, expected)
        }

    @Test
    fun `getTaskById should call routineLocalDataSource getTaskById`() =
        runBlocking {
            val id = 1L
            every { routineLocalDataSource.getTaskByTaskId(id) } returns flowOf(taskEntity1)
            routineRepository.getTaskByTaskId(id)
            verify { routineLocalDataSource.getTaskByTaskId(id) }
        }

    @Test
    fun `insertTask should call routineLocalDataSource insertTask`() =
        runBlocking {
            coEvery { routineLocalDataSource.insertTask(taskEntity1) } returns 1L
            routineRepository.insertTask(task1, 1)
            coVerify { routineLocalDataSource.insertTask(taskEntity1) }
        }

    @Test
    fun `insertTask should return the inserted task id`() =
        runBlocking {
            coEvery { routineLocalDataSource.insertTask(taskEntity1) } returns 1L
            val result = routineRepository.insertTask(task1, 1)
            assertEquals(1L, result)
        }

    @Test
    fun `insertTasks should call routineLocalDataSource insertTasks`() =
        runBlocking {
            coEvery { routineLocalDataSource.insertTasks(tasksEntity1) } returns listOf(1L)
            routineRepository.insertTasks(tasks1, 1)
            coVerify { routineLocalDataSource.insertTasks(tasksEntity1) }
        }

    @Test
    fun `insertTasks should return the inserted task ids`() =
        runBlocking {
            coEvery {
                routineLocalDataSource.insertTasks(tasksEntity1)
            } returns listOf(taskEntity1.id, taskEntity2.id)
            val result = routineRepository.insertTasks(tasks1, 1)
            assertEquals(listOf(1L, 2L), result)
        }

    @Test
    fun `updateTask should call routineLocalDataSource updateTask`() =
        runBlocking {
            coEvery { routineLocalDataSource.updateTask(taskEntity1) } returns Unit
            routineRepository.updateTask(task1, 1)
            coVerify { routineLocalDataSource.updateTask(taskEntity1) }
        }

    @Test
    fun `deleteTaskById should call routineLocalDataSource deleteTaskById`() =
        runBlocking {
            coEvery { routineLocalDataSource.deleteTaskById(1) } returns Unit
            routineRepository.deleteTaskById(1)
            coVerify { routineLocalDataSource.deleteTaskById(1) }
        }

    @Test
    fun `deleteAllTasksByRoutineId should call routineLocalDataSource deleteAllTasksByRoutineId`() =
        runBlocking {
            coEvery { routineLocalDataSource.deleteAllTasksByRoutineId(1) } returns Unit
            routineRepository.deleteAllTasksByRoutineId(1)
            coVerify { routineLocalDataSource.deleteAllTasksByRoutineId(1) }
        }

    @Test
    fun `getRoutinesByName should call routineLocalDataSource getRoutinesByName`() =
        runBlocking {
            coEvery {
                routineLocalDataSource.getRoutinesByName("Routine 1")
            } returns flowOf(listOf(routineWithTasks1))
            routineRepository.getRoutinesByName("Routine 1")
            coVerify { routineLocalDataSource.getRoutinesByName("Routine 1") }
        }

    @Test
    fun `getRoutineByName should return a list of routines`() =
        runBlocking {
            coEvery {
                routineLocalDataSource.getRoutinesByName("Routine 1")
            } returns flowOf(listOf(routineWithTasks1))
            val result = routineRepository.getRoutinesByName("Routine 1").first()
            val expected = listOf(routine1)
        }
}
