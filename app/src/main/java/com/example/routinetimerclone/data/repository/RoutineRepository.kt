package com.example.routinetimerclone.data.repository

import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>

    fun getRoutineById(id: Long): Flow<Routine?>

    fun getRoutineByName(name: String): Flow<Routine?>

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun insertRoutines(routines: List<Routine>): List<Long>

    suspend fun updateRoutine(routine: Routine)

    suspend fun deleteRoutineById(id: Long)

    suspend fun deleteRoutines(routines: List<Routine>)

    fun getTasksByRoutineId(routineId: Long): Flow<List<Task>>

    fun getTaskById(id: Long): Flow<Task?>

    suspend fun insertTask(task: Task): Long

    suspend fun insertTasks(tasks: List<Task>)

    suspend fun updateTask(task: Task)

    suspend fun deleteTaskById(id: Long)

    suspend fun deleteTasksByRoutineId(routineId: Long)

    fun getRoutinesByName(name: String): Flow<List<Routine>>

    fun insertRoutineWithTasks(routines: List<Routine>): Long
}
