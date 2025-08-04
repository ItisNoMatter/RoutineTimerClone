package com.example.routineTimerClone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.routineTimerClone.data.dao.RoutineDao
import com.example.routineTimerClone.data.entitiy.RoutineEntity
import com.example.routineTimerClone.data.entitiy.TaskEntity

@Database(entities = [RoutineEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class EEffortDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
