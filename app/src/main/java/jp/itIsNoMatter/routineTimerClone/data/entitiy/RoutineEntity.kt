package jp.itIsNoMatter.routineTimerClone.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
)
