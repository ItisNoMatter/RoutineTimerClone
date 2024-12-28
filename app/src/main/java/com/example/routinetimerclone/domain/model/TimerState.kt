package com.example.routinetimerclone.domain.model

class TimerState(
    var isRunning: Boolean = false,
    private var remainqSeconds: Int = 0,
    private var totalSeconds: Int = 0,
    val onTimeOver: () -> Unit = {},
) {
    fun start() {
        isRunning = true
    }

    fun pause() {
        isRunning = false
    }

    fun reset() {
        isRunning = false
        remainqSeconds = 0
    }

    fun tick() {
        if (remainqSeconds > 0) {
            remainqSeconds--
        } else {
            onTimeOver()
            isRunning = false
        }
    }

    val remainingDuration
        get() = Duration(remainqSeconds / 60, remainqSeconds % 60)
    val totalDuration
        get() = Duration(totalSeconds / 60, totalSeconds % 60)
    val percentage
        get() = remainqSeconds.toFloat() / totalSeconds.toFloat()
}
