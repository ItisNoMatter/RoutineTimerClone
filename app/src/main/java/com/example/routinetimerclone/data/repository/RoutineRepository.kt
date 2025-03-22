package com.example.routinetimerclone.data.repository

import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutine(id: Long): Flow<Routine?>

    fun getRoutineByName(name: String): Flow<Routine?>

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun insertRoutines(routines: List<Routine>): List<Long>

    suspend fun updateRoutine(routine: Routine)

    suspend fun deleteRoutineById(id: Long)

    fun getTasksByRoutineId(routineId: Long): Flow<List<Task>>

    fun getTaskByTaskId(id: Long): Flow<Task?>

    suspend fun insertTask(
        task: Task,
        parentRoutineId: Long,
    ): Long

    suspend fun insertTasks(
        tasks: List<Task>,
        parentRoutineId: Long,
    ): List<Long>

    suspend fun updateTask(
        task: Task,
        parentRoutineId: Long,
    )

    suspend fun deleteTaskById(id: Long)

    suspend fun deleteAllTasksByRoutineId(routineId: Long)

    fun getRoutinesByName(name: String): Flow<List<Routine>>

    companion object {
        val Fake = FakeRoutineRepository
    }
}

object FakeRoutineRepository : RoutineRepository {
    private val routines =
        listOf(
            Routine(id = 1, name = "Routine 1", tasks = emptyList()),
            Routine(id = 2, name = "Routine 2", tasks = emptyList()),
            Routine(id = 3, name = "Routine 3", tasks = emptyList()),
        )

    override fun getAllRoutines(): Flow<List<Routine>> {
        return flowOf(routines)
    }

    override fun getRoutine(id: Long): Flow<Routine?> {
        return flowOf(routines[id.toInt()])
    }

    override fun getRoutineByName(name: String): Flow<Routine?> {
        return flowOf(routines.find { it.name == name })
    }

    override suspend fun insertRoutine(routine: Routine): Long {
        return routines.size.toLong() + 1
    }

    override suspend fun insertRoutines(routines: List<Routine>): List<Long> {
        return (1..routines.size).map { it.toLong() }
    }

    override suspend fun updateRoutine(routine: Routine) {
        // update
    }

    override suspend fun deleteRoutineById(id: Long) {
        // delete
    }

    override fun getTasksByRoutineId(routineId: Long): Flow<List<Task>> {
        return flowOf(emptyList())
    }

    override fun getTaskByTaskId(id: Long): Flow<Task?> {
        return flowOf(null)
    }

    override suspend fun insertTask(
        task: Task,
        parentRoutineId: Long,
    ): Long {
        return 1
    }

    override suspend fun insertTasks(
        tasks: List<Task>,
        parentRoutineId: Long,
    ): List<Long> {
        return (1..tasks.size).map { it.toLong() }
    }

    override suspend fun updateTask(
        task: Task,
        parentRoutineId: Long,
    ) {
    }

    override suspend fun deleteTaskById(id: Long) {
    }

    override suspend fun deleteAllTasksByRoutineId(routineId: Long) {
    }

    override fun getRoutinesByName(name: String): Flow<List<Routine>> {
        return flowOf(emptyList())
    }
}
