package com.example.routineTimerClone.domain

import com.example.routineTimerClone.domain.model.Duration
import com.example.routineTimerClone.domain.model.TimerState
import org.junit.Test

class TimerStateTest {
    @Test
    fun startTest() {
        val timerState = TimerState()
        assert(!timerState.isRunning)
        timerState.start()
        assert(timerState.isRunning)
    }

    @Test
    fun pauseTest_beforeStart() {
        val timerState = TimerState(isRunning = true)
        timerState.pause()
        assert(!timerState.isRunning)
    }

    @Test
    fun tickTest() {
        val timerState = TimerState(isRunning = true, remainqSeconds = 10)
        timerState.tick()
        assert(timerState.remainingDuration == Duration(0, 9))
    }

    @Test
    fun tickTest_0sec() {
        val timerState = TimerState(isRunning = true, remainqSeconds = 0)
        timerState.tick()
        assert(!timerState.isRunning)
    }

    @Test
    fun resetTest() {
        val timerState = TimerState(isRunning = true, remainqSeconds = 10)
        timerState.reset()
        assert(!timerState.isRunning)
        assert(timerState.remainingDuration == Duration(0, 0))
    }

    @Test
    fun remainingDurationTest() {
        val timerState = TimerState(isRunning = true, remainqSeconds = 10)
        assert(timerState.remainingDuration == Duration(0, 10))
    }

    @Test
    fun totalDurationTest() {
        val timerState = TimerState(isRunning = true, totalSeconds = 10)
        assert(timerState.totalDuration == Duration(0, 10))
    }

    @Test
    fun percentageTest() {
        val timerState = TimerState(isRunning = true, remainqSeconds = 10, totalSeconds = 20)
        assert(timerState.percentage == 0.5f)
    }
}
