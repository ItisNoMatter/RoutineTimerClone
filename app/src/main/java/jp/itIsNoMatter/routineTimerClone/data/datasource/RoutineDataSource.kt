package jp.itIsNoMatter.routineTimerClone.data.datasource

import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {
    fun getAllRoutines(): Flow<List<RoutineWithTasks>>

    fun getRoutineById(id: String): Flow<RoutineWithTasks?>

    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    suspend fun insertRoutine(routine: RoutineEntity)

    suspend fun insertRoutines(routines: List<RoutineEntity>)

    suspend fun insertTask(task: TaskEntity)

    suspend fun insertTasks(tasks: List<TaskEntity>)

    fun getTasksByRoutineId(id: String): Flow<List<TaskEntity>>

    fun getTaskByTaskId(id: String): Flow<TaskEntity>

    suspend fun deleteAllTasksByRoutineId(id: String)

    suspend fun deleteRoutineById(id: String)

    suspend fun deleteTaskById(id: String)

    // ※ DAO側ではInt（更新行数）を返していましたが、
    // DataSourceやRepositoryで使わないなら Unit のままで全く問題ありません。
    suspend fun updateRoutine(routine: RoutineEntity)

    suspend fun updateTask(task: TaskEntity)

    suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): String

    suspend fun updateRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    )
}
