package jp.itIsNoMatter.routineTimerClone.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "routine")
data class RoutineEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
)
