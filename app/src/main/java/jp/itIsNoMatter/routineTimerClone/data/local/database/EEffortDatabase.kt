package jp.itIsNoMatter.routineTimerClone.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.itIsNoMatter.routineTimerClone.data.local.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.local.entity.TaskEntity

@Database(entities = [RoutineEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class EEffortDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
