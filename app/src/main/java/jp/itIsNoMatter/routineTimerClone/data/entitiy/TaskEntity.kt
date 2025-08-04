package jp.itIsNoMatter.routineTimerClone.data.entitiy

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    indices = [androidx.room.Index("parentRoutineId")],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val seconds: Int,
    val parentRoutineId: Long,
)
