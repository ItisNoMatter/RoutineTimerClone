package jp.itIsNoMatter.routineTimerClone.domain.model

data class TimerState(
    val isRunning: Boolean,
    private val remainSeconds: Int,
    private val totalSeconds: Int,
    val onTimeOver: () -> Unit,
    val onHalfTime: () -> Unit,
    private val isHalfTimeNotified: Boolean = false,
) {
    fun start(): TimerState {
        return this.copy(isRunning = true)
    }

    fun pause(): TimerState {
        return this.copy(isRunning = false)
    }

    fun tick(): TimerState {
        if (!isRunning) return this

        val nextRemainSeconds = remainSeconds - 1

        if (nextRemainSeconds <= 0) {
            onTimeOver()
            return this.copy(isRunning = false, remainSeconds = 0, isHalfTimeNotified = true)
        }

        if (!isHalfTimeNotified && nextRemainSeconds < totalSeconds / 2) {
            onHalfTime()
            return this.copy(
                remainSeconds = nextRemainSeconds,
                isHalfTimeNotified = true,
            )
        }

        // 通常のtick
        return this.copy(remainSeconds = nextRemainSeconds)
    }

    val remainingDuration
        get() = Duration(remainSeconds / 60, remainSeconds % 60)
    val totalDuration
        get() = Duration(totalSeconds / 60, totalSeconds % 60)

    // percentageの分母が0になる可能性を考慮
    val percentage
        get() = if (totalSeconds > 0) remainSeconds.toFloat() / totalSeconds.toFloat() else 0f

    companion object {
        val Unload =
            TimerState(
                isRunning = false,
                remainSeconds = 0,
                totalSeconds = 0,
                onTimeOver = {},
                onHalfTime = {},
            )
    }
}
