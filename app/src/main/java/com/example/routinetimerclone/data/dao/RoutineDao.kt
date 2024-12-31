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
    fun getRoutineById(id: Int): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name = :name")
    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name LIKE '%' || :name || '%'")
    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutines(routines: List<RoutineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Transaction
    suspend fun insertTasks(
        tasks: List<TaskEntity>,
        parentRoutineId: Long,
    ) {
        if (!tasks.all { it.id == parentRoutineId })return
        for (task in tasks) {
            insertTask(task)
        }
    }

    @Transaction
    suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ) {
        val routineId = insertRoutine(routine)
        insertTasks(tasks, routineId)
    }

    @Query("SELECT * FROM task WHERE parentRoutineId = :id")
    fun getTasksByRoutineId(id: Int): Flow<List<TaskEntity>>

    @Query("DELETE FROM task WHERE parentRoutineId = :id")
    suspend fun deleteTasksByRoutineId(id: Int)

    @Query("DELETE FROM routine WHERE id = :id")
    suspend fun deleteRoutineById(id: Int)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)
}
