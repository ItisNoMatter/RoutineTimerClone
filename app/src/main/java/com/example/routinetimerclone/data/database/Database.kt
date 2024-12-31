package com.example.routinetimerclone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.TaskEntity

@Database(entities = [RoutineEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
