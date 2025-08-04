package jp.itIsNoMatter.routineTimerClone.data.datasource

import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {
    fun getAllRoutines(): Flow<List<RoutineWithTasks>>

    fun getRoutineById(id: Long): Flow<RoutineWithTasks?>

    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    suspend fun insertRoutine(routine: RoutineEntity): Long

    suspend fun insertRoutines(routines: List<RoutineEntity>): List<Long>

    suspend fun insertTask(task: TaskEntity): Long

    suspend fun insertTasks(tasks: List<TaskEntity>): List<Long>

    fun getTasksByRoutineId(id: Long): Flow<List<TaskEntity>>

    fun getTaskByTaskId(id: Long): Flow<TaskEntity>

    suspend fun deleteAllTasksByRoutineId(id: Long)

    suspend fun deleteRoutineById(id: Long)

    suspend fun deleteTaskById(id: Long)

    suspend fun updateRoutine(routine: RoutineEntity)

    suspend fun updateTask(task: TaskEntity)

    suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): Long

    suspend fun updateRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    )
}
