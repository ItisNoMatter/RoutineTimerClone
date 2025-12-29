package jp.itIsNoMatter.routineTimerClone.domain.model

data class TimerState(
    val isRunning: Boolean,
    private val remainSeconds: Int,
    private val totalSeconds: Int,
    val onTimeOver: () -> Unit,
) {
    fun start(): TimerState {
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

    companion object {
        val Unload =
            TimerState(
                isRunning = false,
                remainSeconds = 0,
                totalSeconds = 0,
                onTimeOver = {},
            )
    }
}
