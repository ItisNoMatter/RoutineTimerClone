package jp.itIsNoMatter.routineTimerClone.data.repository

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutine(id: String): Flow<LoadedValue<Routine>>

    fun getRoutineByName(name: String): Flow<Routine?> // TODO return LoadedValue

    suspend fun insertRoutine(routine: Routine)

    suspend fun insertRoutines(routines: List<Routine>)

    suspend fun updateRoutine(routine: Routine)

    suspend fun deleteRoutineById(id: String)

    fun getTasksByRoutineId(routineId: String): Flow<List<Task>>

    fun getTaskByTaskId(id: String): Flow<Task?>

    suspend fun insertTask(
        task: Task,
        parentRoutineId: String,
    )

    suspend fun insertTasks(
        tasks: List<Task>,
        parentRoutineId: String,
    )

    suspend fun updateTask(
        task: Task,
        parentRoutineId: String,
    )

    suspend fun deleteTaskById(id: String)

    suspend fun deleteAllTasksByRoutineId(routineId: String)

    fun getRoutinesByName(name: String): Flow<List<Routine>>

    suspend fun syncRoutines()
}

object FakeRoutineRepository : RoutineRepository {
    private val routines =
        listOf(
            Routine(id = "1", name = "Routine 1", tasks = emptyList()),
            Routine(id = "2", name = "Routine 2", tasks = emptyList()),
            Routine(id = "3", name = "Routine 3", tasks = emptyList()),
        )

    override fun getAllRoutines(): Flow<List<Routine>> {
        return flowOf(routines)
    }

    override fun getRoutine(id: String): Flow<LoadedValue<Routine>> {
        val routine = routines.first { it.id == id }
        return flowOf(LoadedValue.Done(routine))
    }

    override fun getRoutineByName(name: String): Flow<Routine?> {
        return flowOf(routines.find { it.name == name })
    }

    override suspend fun insertRoutine(routine: Routine) {
        // 戻り値が不要になったので空でOK！
    }

    override suspend fun insertRoutines(routines: List<Routine>) {
    }

    override suspend fun updateRoutine(routine: Routine) {
    }

    override suspend fun deleteRoutineById(id: String) {
    }

    override fun getTasksByRoutineId(routineId: String): Flow<List<Task>> {
        return flowOf(emptyList())
    }

    override fun getTaskByTaskId(id: String): Flow<Task?> {
        return flowOf(null)
    }

    override suspend fun insertTask(
        task: Task,
        parentRoutineId: String,
    ) {
    }

    override suspend fun insertTasks(
        tasks: List<Task>,
        parentRoutineId: String,
    ) {
    }

    override suspend fun updateTask(
        task: Task,
        parentRoutineId: String,
    ) {
    }

    override suspend fun deleteTaskById(id: String) {
    }

    override suspend fun deleteAllTasksByRoutineId(routineId: String) {
    }

    override fun getRoutinesByName(name: String): Flow<List<Routine>> {
        return flowOf(emptyList())
    }

    override suspend fun syncRoutines() {
    }
}
