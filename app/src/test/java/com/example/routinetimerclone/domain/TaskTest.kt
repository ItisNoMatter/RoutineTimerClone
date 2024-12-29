package com.example.routinetimerclone.domain

import com.example.routinetimerclone.domain.model.Duration
import com.example.routinetimerclone.domain.model.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskTest {
    @Test
    fun totalSecondsCalculationTest() {
        val task = Task(1, "Task 1", Duration(1, 30))
        assertEquals(90, task.duration.getTotalSeconds())
    }

    @Test
    fun secondaryConstructorTest() {
        val primary = Task(1, "Task 1", Duration(1, 30))
        val secondary = Task(1, "Task 1", 1, 30)
        assertEquals(primary, secondary)
    }

    @Test
    fun getterTest() {
        val task = Task(1, "Task 1", Duration(1, 30))
        assertEquals(1, task.minutes)
        assertEquals(30, task.seconds)
    }
}
