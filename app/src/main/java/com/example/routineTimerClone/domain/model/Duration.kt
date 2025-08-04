package com.example.routineTimerClone.domain.model

import java.util.Locale

data class Duration(
    val minutes: Int,
    val seconds: Int,
) {
    init {
        require(minutes >= 0) { "Minute cannot be negative" }
        require(seconds >= 0) { "Second cannot be negative" }
        require(seconds < 60) { "Second cannot be greater than 59" }
    }

    fun getTotalSeconds(): Int {
        return minutes * 60 + seconds
    }

    fun toDisplayString(): String {
        return String.format(Locale.JAPAN, "%2d分%02d秒", minutes, seconds)
    }

    companion object {
        fun fromSeconds(seconds: Int): Duration {
            return Duration(seconds / 60, seconds % 60)
        }
    }
}
