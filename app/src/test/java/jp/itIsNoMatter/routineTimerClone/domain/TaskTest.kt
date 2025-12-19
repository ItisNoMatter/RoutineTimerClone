package jp.itIsNoMatter.routineTimerClone.domain

import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskTest {
    @Test
    fun totalSecondsCalculationTest() {
        val task = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        assertEquals(90, task.duration.getTotalSeconds())
    }

    @Test
    fun secondaryConstructorTest() {
        val primary = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        val secondary = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        assertEquals(primary, secondary)
    }

    @Test
    fun getterTest() {
        val task = Task(id = 1, name = "Task 1", minutes = 1, seconds = 30, announceRemainingTimeFlag = true)
        assertEquals(1, task.minutes)
        assertEquals(30, task.seconds)
    }
}
