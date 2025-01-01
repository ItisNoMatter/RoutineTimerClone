package com.example.routinetimerclone.data.repository

import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutine(id: Long): Flow<Routine?>

    fun getRoutinesByName(name: String): Flow<Routine?>

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun insertRoutines(routines: List<Routine>): List<Long>

    suspend fun updateRoutine(routine: Routine)

    suspend fun deleteRoutineById(id: Long)

    fun getTasksByRoutineId(routineId: Long): Flow<List<Task>>

    fun getTaskById(id: Long): Flow<Task?>

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
}
