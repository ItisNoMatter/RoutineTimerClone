package jp.itIsNoMatter.routineTimerClone.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentRoutineId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("parentRoutineId")],
)
data class TaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val seconds: Int,
    val parentRoutineId: String,
    val announceRemainingTimeFlag: Boolean = true,
    val orderIndex: Int = 0,
)
