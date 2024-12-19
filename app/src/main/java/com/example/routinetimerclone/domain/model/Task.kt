package com.example.routinetimerclone.domain.model

data class Task(
    val name:String,
    val duration: Duration
){
    constructor(name:String, minutes:Int, seconds:Int):this(name, Duration(minutes, seconds))
    val minutes:Int
        get() = duration.minutes
    val seconds:Int
        get() = duration.seconds
}