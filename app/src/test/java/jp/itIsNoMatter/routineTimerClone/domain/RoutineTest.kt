package jp.itIsNoMatter.routineTimerClone.domain

import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
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
    fun taskSwapTest() {
        val task1 = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        val task2 = Task(id = 2, name = "Task 2", minutes = 2, seconds = 0, announceRemainingTimeFlag = true)
        val task3 = Task(id = 3, name = "Task 3", minutes = 3, seconds = 40, announceRemainingTimeFlag = true)
        val routine = Routine(1, "Test Routine", listOf(task1, task2, task3))
        val movedRoutine = routine.taskSwap(0, 1)
        assertEquals(movedRoutine.tasks[0], task2)
        assertEquals(movedRoutine.tasks[1], task1)
        assertEquals(movedRoutine.tasks[2], task3)
    }

    @Test
    fun totalDurationCalculationTest() {
        val task1 = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        val task2 = Task(id = 2, name = "Task 2", minutes = 2, seconds = 0, announceRemainingTimeFlag = true)
        val task3 = Task(id = 3, name = "Task 3", minutes = 3, seconds = 40, announceRemainingTimeFlag = true)
        val routine = Routine(1, "Test Routine", listOf(task1, task2, task3))
        val (minutes, seconds) = routine.getTotalDuration()
        assertEquals(7, minutes)
        assertEquals(10, seconds)
    }
}
