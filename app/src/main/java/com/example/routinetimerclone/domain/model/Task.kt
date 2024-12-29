package com.example.routinetimerclone.domain.model

data class Task(
    val id: Int,
    val name: String,
    val duration: Duration,
) {
    constructor(
        id: Int,
        name: String,
        minutes: Int,
        seconds: Int,
    ) : this(id, name, Duration(minutes, seconds))

    val minutes: Int
        get() = duration.minutes
    val seconds: Int
        get() = duration.seconds
}
