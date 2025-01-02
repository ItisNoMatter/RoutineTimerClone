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
            // Arrange
            val routineWithTasks = listOf(RoutineWithTasks(routine = RoutineEntity(1, "Routine 1"), tasks = emptyList()))
            every { routineLocalDataSource.getAllRoutines() } returns flowOf(routineWithTasks)
            every { routineModelMapper.toDomain(any(), any()) } returns Routine(1, "Routine 1", emptyList())

            // Act
            val result = routineRepository.getAllRoutines().toList()
            val expected = listOf(Routine(1, "Routine 1", emptyList()))
            // Assert
            assertEquals(expected, result)
        }
}
