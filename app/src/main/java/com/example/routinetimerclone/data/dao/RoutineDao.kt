package com.example.routinetimerclone.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Transaction
    @Query("SELECT * FROM routine")
    fun getAllRoutines(): Flow<List<RoutineWithTasks>>

    @Transaction
    @Query("SELECT * FROM routine WHERE id = :id")
    fun getRoutineById(id: Long): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name = :name")
    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name LIKE '%' || :name || '%'")
    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutines(routines: List<RoutineEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Query("SELECT * FROM task WHERE parentRoutineId = :id")
    fun getTasksByRoutineId(id: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskByTaskId(id: Long): Flow<TaskEntity>

    @Query("DELETE FROM task WHERE parentRoutineId = :id")
    suspend fun deleteAllTasksByRoutineId(id: Long)

    @Query("DELETE FROM routine WHERE id = :id")
    suspend fun deleteRoutineById(id: Long)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Update
    suspend fun updateRoutine(routine: RoutineEntity): Int

    @Update
    suspend fun updateTask(task: TaskEntity): Int

    @Transaction
    suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): Long {
        val routineId = insertRoutine(routine)
        for (task in tasks) {
            insertTask(task.copy(parentRoutineId = routineId))
        }
        return routineId
    }

    @Transaction
    suspend fun updateRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): Int {
        val updatedRows = updateRoutine(routine)
        deleteAllTasksByRoutineId(routine.id)
        for (task in tasks) {
            insertTask(task.copy(parentRoutineId = routine.id))
        }
        return updatedRows
    }
}
