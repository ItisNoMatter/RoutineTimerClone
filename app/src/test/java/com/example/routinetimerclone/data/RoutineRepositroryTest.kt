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
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
    fun setup()  {
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
    fun `getAllRoutines should call routineLocalDataSource getAllRoutines`()  {
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
            every { routineModelMapper.toDomain(RoutineEntity(1,"Routine 1"), emptyList()) } returns Routine(1, "Routine 1", emptyList())
            every { routineModelMapper.toDomain(RoutineEntity(2,"Routine 2"), emptyList()) } returns Routine(2, "Routine 2", emptyList())

            // Act
            val result = routineRepository.getAllRoutines().toList()[0]
            val expected = listOf(Routine(1, "Routine 1", emptyList()), Routine(2, "Routine 2", emptyList()))
            // Assert
            assertEquals(expected, result)
        }
}
