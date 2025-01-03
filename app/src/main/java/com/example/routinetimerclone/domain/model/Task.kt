package com.example.routinetimerclone.domain.model

data class Task(
    val id: Long,
    val name: String,
    val duration: Duration,
) {
    constructor(
        id: Long,
        name: String,
        minutes: Int,
        seconds: Int,
    ) : this(id, name, Duration(minutes, seconds))

    val minutes: Int
        get() = duration.minutes
    val seconds: Int
        get() = duration.seconds
}
