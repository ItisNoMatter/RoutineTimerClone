package com.example.routinetimerclone.data

import com.example.routinetimerclone.data.datasource.RoutineLocalDataSource
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.mapper.RoutineModelMapper
import com.example.routinetimerclone.data.entitiy.mapper.TaskModelMapper
import com.example.routinetimerclone.data.repository.RoutineRepository
import com.example.routinetimerclone.data.repository.RoutineRepositoryImpl
import com.example.routinetimerclone.domain.model.Routine
import com.google.common.base.Verify.verify
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    @Before
    fun setup() {
        routineModelMapper = mockk()
        taskModelMapper = mockk()
        routineLocalDataSource = mockk()
        routineRepository = mockk()

        routineRepository =
            RoutineRepositoryImpl(
                routineLocalDataSource,
                routineModelMapper,
                taskModelMapper,
            )
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
            val routineEntity1 = RoutineEntity(1, "Routine 1")
            val routineEntity2 = RoutineEntity(2, "Routine 2")
            val routineWithTasks1 = RoutineWithTasks(routine = routineEntity1, tasks = emptyList())
            val routineWithTasks2 = RoutineWithTasks(routine = routineEntity2, tasks = emptyList())
            val routinesWithTasks = listOf(routineWithTasks1, routineWithTasks2)

            // Arrange
            every { routineLocalDataSource.getAllRoutines() } returns flowOf(routinesWithTasks)
            every { routineModelMapper.toDomain(routineEntity1, emptyList()) } returns Routine(1, "Routine 1", emptyList())
            every { routineModelMapper.toDomain(routineEntity2, emptyList()) } returns Routine(2, "Routine 2", emptyList())

            // Act
            val result = routineRepository.getAllRoutines().first()
            val expected = listOf(Routine(1, "Routine 1", emptyList()), Routine(2, "Routine 2", emptyList()))
            // Assert
            assertEquals(expected, result)
        }

    @Test
    fun `getRoutine should call routineLocalDataSource getRoutineById`() =
        runBlocking {
            val id = 1L
            val mockResponse = flowOf<RoutineWithTasks?>(null)
            every { routineLocalDataSource.getRoutineById(id) } returns mockResponse
            routineRepository.getRoutine(id)
            verify { routineLocalDataSource.getRoutineById(id) }
        }

    @Test
    fun `getRoutine should return a routine`() =
        runBlocking {
            val id = 1L
            val routineEntity = RoutineEntity(id, "Routine 1")
            val routineWithTasks = RoutineWithTasks(routine = routineEntity, tasks = emptyList())
            every { routineLocalDataSource.getRoutineById(id) } returns flowOf(routineWithTasks)
            every { routineModelMapper.toDomain(routineEntity, emptyList()) } returns Routine(id, "Routine 1", emptyList())
            val result = routineRepository.getRoutine(id).first()
            val expected = Routine(id, "Routine 1", emptyList())
            assertEquals(expected, result)
        }

    @Test
    fun `getRoutineByName should call routineLocalDataSource getRoutineByName`() =
        runBlocking {
            val name = "Routine 1"
            val mockResponse = flowOf<RoutineWithTasks?>(null)
            every { routineLocalDataSource.getRoutineByName(name) } returns mockResponse
            every { routineModelMapper.toDomain(any(), any()) } returns Routine(1, "Routine 1", emptyList())
            routineRepository.getRoutineByName(name)
            verify { routineLocalDataSource.getRoutineByName(name) }
        }

    @Test
    fun `insertRoutine should call routineLocalDataSource insertRoutineWithTasks`() =
        runBlocking {
            val routine = Routine(1, "Routine 1", emptyList())
            val routineEntity = RoutineEntity(1, "Routine 1")
            val routineWithTasks = RoutineWithTasks(routine = routineEntity, tasks = emptyList())
            every { routineModelMapper.toEntity(routine) } returns routineWithTasks
            coEvery { routineLocalDataSource.insertRoutineWithTasks(any(), any()) } returns 1L
            routineRepository.insertRoutine(routine)
            coVerify { routineLocalDataSource.insertRoutineWithTasks(any(), any()) }
        }
}
