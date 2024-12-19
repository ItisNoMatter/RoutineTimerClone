package com.example.routinetimerclone.domain.model

data class Duration (
    val minutes: Int,
    val seconds: Int,
){
    init{
        require(minutes >= 0) { "Minute cannot be negative" }
        require(seconds >= 0) { "Second cannot be negative" }
        require(seconds < 60) { "Second cannot be greater than 59" }
    }
    fun getTotalSeconds():Int{
        return minutes * 60 + seconds
    }
}