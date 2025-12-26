package jp.itIsNoMatter.routineTimerClone.domain.model

data class TimerState(
    val isRunning: Boolean = false,
    private val remainSeconds: Int = 0,
    private val totalSeconds: Int = 0,
    val onTimeOver: () -> Unit = {},
) {
    fun start(): TimerState {
        if (isRunning) return this
        return this.copy(isRunning = true)
    }

    fun pause(): TimerState {
        return this.copy(isRunning = false)
    }

    fun tick(): TimerState {
        if (!isRunning) return this

        return if (remainSeconds > 0) {
            this.copy(remainSeconds = remainSeconds - 1)
        } else {
            onTimeOver()
            this.copy(isRunning = false, remainSeconds = 0)
        }
    }

    val remainingDuration
        get() = Duration(remainSeconds / 60, remainSeconds % 60)
    val totalDuration
        get() = Duration(totalSeconds / 60, totalSeconds % 60)
    val percentage
        get() = remainSeconds.toFloat() / totalSeconds.toFloat()
}
