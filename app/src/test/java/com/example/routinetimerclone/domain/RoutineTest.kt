package com.example.routinetimerclone.domain

import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class RoutineTest {
    @Test
    fun totalDurationCalculationTest_empty() {
        val routine = Routine(1, "Test Routine", emptyList())
        val (minutes, seconds) = routine.getTotalDuration()
        assertEquals(0, minutes)
        assertEquals(0, seconds)
    }

    @Test
    fun taskMovedTest() {
        val task1 = Task(1, "Task 1", 1, 30)
        val task2 = Task(2, "Task 2", 2, 0)
        val task3 = Task(3, "Task 3", 3, 30)
        val routine = Routine(1, "Test Routine", listOf(task1, task2, task3))
        val movedRoutine = routine.taskMoved(0, 1)
        assertEquals(movedRoutine.tasks[0], task2)
        assertEquals(movedRoutine.tasks[1], task1)
        assertEquals(movedRoutine.tasks[2], task3)
    }

    @Test
    fun totalDurationCalculationTest() {
        val task1 = Task(1, "Task 1", 1, 30)
        val task2 = Task(2, "Task 2", 2, 0)
        val task3 = Task(3, "Task 3", 3, 40)
        val routine = Routine(1, "Test Routine", listOf(task1, task2, task3))
        val (minutes, seconds) = routine.getTotalDuration()
        assertEquals(7, minutes)
        assertEquals(10, seconds)
    }
}
