package com.example.routinetimerclone.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val seconds: Int,
    val minutes: Int,
)
