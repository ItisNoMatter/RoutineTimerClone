package jp.itIsNoMatter.routineTimerClone.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.itIsNoMatter.routineTimerClone.data.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity

@Database(entities = [RoutineEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class EEffortDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
