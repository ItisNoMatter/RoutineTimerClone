package com.example.routinetimerclone.domain

import com.example.routinetimerclone.domain.model.Duration
import org.junit.Assert.assertEquals
import org.junit.Test

class DurationTest{
    @Test
    fun totalSecondsCalculationTest(){
        val duration = Duration(1,30)
        assertEquals(90, duration.getTotalSeconds())
    }
    @Test
    fun totalSecondsCalculationTest_0min(){
        val duration = Duration(0,30)
        assertEquals(30, duration.getTotalSeconds())
    }
    @Test
    fun totalSecondsCalculationTest_0sec(){
        val duration = Duration(1,0)
        assertEquals(60, duration.getTotalSeconds())
    }
    @Test
    fun totalSecondsCalculationTest_0min0sec(){
        val duration = Duration(0,0)
        assertEquals(0, duration.getTotalSeconds())
    }
}