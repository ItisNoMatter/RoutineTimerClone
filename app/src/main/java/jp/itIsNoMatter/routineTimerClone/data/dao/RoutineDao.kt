package jp.itIsNoMatter.routineTimerClone.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Transaction
    @Query("SELECT * FROM routine")
    fun getAllRoutines(): Flow<List<RoutineWithTasks>>

    @Transaction
    @Query("SELECT * FROM routine WHERE id = :id")
    fun getRoutineById(id: String): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name = :name")
    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routine WHERE name LIKE '%' || :name || '%'")
    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutines(routines: List<RoutineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task WHERE parentRoutineId = :id")
    fun getTasksByRoutineId(id: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskByTaskId(id: String): Flow<TaskEntity>

    @Query("DELETE FROM task WHERE parentRoutineId = :id")
    suspend fun deleteAllTasksByRoutineId(id: String)

    @Query("DELETE FROM routine WHERE id = :id")
    suspend fun deleteRoutineById(id: String)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: String)

    @Update
    suspend fun updateRoutine(routine: RoutineEntity): Int

    @Update
    suspend fun updateTask(task: TaskEntity): Int

    @Transaction
    suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): String {
        insertRoutine(routine)
        for (task in tasks) {
            // routineのインスタンスには既にUUIDが入っているので、そのまま routine.id を使えばOK！
            insertTask(task.copy(parentRoutineId = routine.id))
        }
        return routine.id
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
        return updatedRows // 更新された行数を返す
    }
}
