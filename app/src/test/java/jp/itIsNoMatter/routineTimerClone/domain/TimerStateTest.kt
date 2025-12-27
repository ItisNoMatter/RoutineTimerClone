package jp.itIsNoMatter.routineTimerClone.domain

import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.TimerState
import org.junit.Test

class TimerStateTest {
    @Test
    fun startTest() {
        val timerState = TimerState()
        assert(!timerState.isRunning)
        assert(timerState.start().isRunning)
    }

    @Test
    fun pauseTest_beforeStart() {
        val timerState = TimerState(isRunning = true)

        assert(!timerState.pause().isRunning)
    }

    @Test
    fun tickTest() {
        val timerState = TimerState(isRunning = true, remainSeconds = 10)

        assert(timerState.tick().remainingDuration == Duration(0, 9))
    }

    @Test
    fun tickTest_0sec() {
        val timerState = TimerState(isRunning = true, remainSeconds = 0)

        assert(!timerState.tick().isRunning)
    }

    @Test
    fun remainingDurationTest() {
        val timerState = TimerState(isRunning = true, remainSeconds = 10)
        assert(timerState.remainingDuration == Duration(0, 10))
    }

    @Test
    fun totalDurationTest() {
        val timerState = TimerState(isRunning = true, totalSeconds = 10)
        assert(timerState.totalDuration == Duration(0, 10))
    }

    @Test
    fun percentageTest() {
        val timerState = TimerState(isRunning = true, remainSeconds = 10, totalSeconds = 20)
        assert(timerState.percentage == 0.5f)
    }
}
