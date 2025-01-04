package com.example.routinetimerclone.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.database.EEfortDatabase
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.TaskEntity
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RoutineDaoTest {
    val testDispatcher = StandardTestDispatcher()
    private lateinit var db: EEfortDatabase
    private lateinit var dao: RoutineDao

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), EEfortDatabase::class.java)
                .setQueryExecutor(testDispatcher.asExecutor())
                .setTransactionExecutor(testDispatcher.asExecutor())
                .allowMainThreadQueries()
                .build()
        dao = db.routineDao()
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun insertRoutineTest() =
        runTest(testDispatcher.scheduler) {
            val routine1 = RoutineEntity(1, "Test Routine 1")
            val routine2 = RoutineEntity(2, "Test Routine 2")
            val id1 = dao.insertRoutine(routine1)
            advanceUntilIdle()
            assert(routine1.id == id1)
            val id2 = dao.insertRoutine(routine2)
            advanceUntilIdle()
            assert(routine2.id == id2)
        }

    @Test
    fun insertRoutinesTest() =
        runTest(testDispatcher.scheduler) {
            val routine1 = RoutineEntity(1, "Test Routine 1")
            val routine2 = RoutineEntity(2, "Test Routine 2")
            val routines = listOf(routine1, routine2)
            dao.insertRoutines(routines)
            advanceUntilIdle()
            val result = dao.getAllRoutines().first().map { it.routine }
            assertEquals(routines, result)
        }

    @Test
    fun insertTaskTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val task = TaskEntity(0, "Test Task", 60, routineId)
            val expected = TaskEntity(1, "Test Task", 60, routineId)
            advanceUntilIdle()
            dao.insertTask(task)
            advanceUntilIdle()
            val result = dao.getAllRoutines().first()[0]
            advanceUntilIdle()
            assertEquals(expected, result.tasks[0])
        }

    @Test
    fun getRoutineByIdTest() =
        runTest(testDispatcher.scheduler) {
            val name = "Test Routine"
            val routine = RoutineEntity(0, name)
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val result = dao.getRoutineById(routineId).first()
            advanceUntilIdle()
            assert(result != null)
            assert(result?.routine != null)
            assertEquals(name, result?.routine?.name)
        }

    @Test
    fun insertTasksTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val tasks =
                listOf(
                    TaskEntity(0, "Test Task 1", 60, routineId),
                    TaskEntity(0, "Test Task 2", 120, routineId),
                )
            tasks.forEach {
                dao.insertTask(it)
                advanceUntilIdle()
            }
            val result = dao.getRoutineById(routineId).first()
            advanceUntilIdle()
            assert(result != null)
            assert(result?.tasks != null)
            val expectedTasks =
                listOf(
                    TaskEntity(1, "Test Task 1", 60, routineId),
                    TaskEntity(2, "Test Task 2", 120, routineId),
                )
            assertEquals(expectedTasks, result?.tasks)
        }

    @Test
    fun getTasksByRoutineIdTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val tasks =
                listOf(
                    TaskEntity(0, "Test Task 1", 60, routineId),
                    TaskEntity(0, "Test Task 2", 120, routineId),
                )
            tasks.forEach {
                dao.insertTask(it)
                advanceUntilIdle()
            }
            val result = dao.getTasksByRoutineId(routineId).first()
            advanceUntilIdle()
            val expectedTasks =
                listOf(
                    TaskEntity(1, "Test Task 1", 60, routineId),
                    TaskEntity(2, "Test Task 2", 120, routineId),
                )
            assertEquals(expectedTasks, result)
        }

    @Test
    fun getRoutinesByNameTest() =
        runTest(testDispatcher.scheduler) {
            val routine1 = RoutineEntity(0, "Test Routine 1")
            val routine2 = RoutineEntity(0, "Test Routine 2")
            val routines = listOf(routine1, routine2)
            dao.insertRoutines(routines)
            advanceUntilIdle()
            val result = dao.getRoutinesByName("1").first()
            advanceUntilIdle()

            val expectedRoutines =
                listOf(
                    RoutineEntity(1, "Test Routine 1"),
                )
            assertEquals(expectedRoutines, result.map { it.routine })
        }

    @Test
    fun deleteAllTasksByRoutineIdTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val tasks =
                listOf(
                    TaskEntity(0, "Test Task 1", 60, routineId),
                    TaskEntity(0, "Test Task 2", 120, routineId),
                )
            tasks.forEach {
                dao.insertTask(it)
                advanceUntilIdle()
            }
            dao.deleteAllTasksByRoutineId(routineId)
            advanceUntilIdle()
            val result = dao.getTasksByRoutineId(routineId).first()
            advanceUntilIdle()
            assert(result.isEmpty())
        }

    @Test
    fun deleteRoutineByIdTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            assert(dao.getRoutineById(routineId).first() != null)
            dao.deleteRoutineById(routineId)
            advanceUntilIdle()
            assert(dao.getRoutineById(routineId).first() == null)
        }

    @Test
    fun deleteTaskByIdTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val taskId = dao.insertTask(TaskEntity(0, "Test Task", 60, routineId))
            advanceUntilIdle()
            assert(dao.getTasksByRoutineId(routineId).first().isNotEmpty())
            dao.deleteTaskById(taskId)
            advanceUntilIdle()
            assert(dao.getTasksByRoutineId(routineId).first().isEmpty())
        }

    @Test
    fun updateRoutineTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val updatedRoutine = RoutineEntity(routineId, "Updated Routine")
            dao.updateRoutine(updatedRoutine)
            advanceUntilIdle()
            val result = dao.getRoutineById(routineId).first()
            advanceUntilIdle()
            assert(result != null)
            assert(result?.routine != null)
            assertEquals("Updated Routine", result?.routine?.name)
        }

    @Test
    fun updateRoutine_withTasksTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val tasks =
                listOf(
                    TaskEntity(0, "Test Task 1", 60, routineId),
                    TaskEntity(0, "Test Task 2", 120, routineId),
                )
            tasks.forEach {
                dao.insertTask(it)
                advanceUntilIdle()
            }
            val updatedRoutine = RoutineEntity(routineId, "Updated Routine")
            dao.updateRoutine(updatedRoutine)
            advanceUntilIdle()
            val result = dao.getRoutineById(routineId).first()
            advanceUntilIdle()
            assert(result != null)
            assert(result?.routine != null)
            assertEquals("Updated Routine", result?.routine?.name)
            assert(result?.tasks != null)
            val expectedTasks =
                listOf(
                    TaskEntity(1, "Test Task 1", 60, 1),
                    TaskEntity(2, "Test Task 2", 120, 1),
                )
            assertEquals(expectedTasks, result?.tasks)
        }

    @Test
    fun updateTaskTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val routineId = dao.insertRoutine(routine)
            advanceUntilIdle()
            val taskId = dao.insertTask(TaskEntity(0, "Test Task", 60, routineId))
            advanceUntilIdle()
            val updatedTask = TaskEntity(taskId, "Updated Task", 120, routineId)
            dao.updateTask(updatedTask)
            advanceUntilIdle()
            val result = dao.getTaskById(taskId).first()
            advanceUntilIdle()
            assertEquals(result[0], updatedTask)
        }

    @Test
    fun insertRoutineWithTasksTest() =
        runTest(testDispatcher.scheduler) {
            val routine = RoutineEntity(0, "Test Routine")
            val tasks =
                listOf(
                    TaskEntity(0, "Test Task 1", 60, 0),
                    TaskEntity(0, "Test Task 2", 120, 0),
                )
            dao.insertRoutineWithTasks(routine, tasks)
            advanceUntilIdle()
            val result = dao.getAllRoutines().first()
            advanceUntilIdle()
            assert(result.isNotEmpty())
        }
}
