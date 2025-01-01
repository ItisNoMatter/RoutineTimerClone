package com.example.routinetimerclone.data.datasource

import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {
    fun getAllRoutines(): Flow<List<RoutineWithTasks>>

    fun getRoutineById(id: Long): Flow<RoutineWithTasks?>

    fun getRoutineByName(name: String): Flow<RoutineWithTasks?>

    fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>>

    suspend fun insertRoutine(routine: RoutineEntity): Long

    suspend fun insertRoutines(routines: List<RoutineEntity>)

    suspend fun insertTask(task: TaskEntity): Long

    fun getTasksByRoutineId(id: Long): Flow<List<TaskEntity>>

    fun getTaskById(id: Long): Flow<List<TaskEntity>>

    suspend fun deleteTasksByRoutineId(id: Long)

    suspend fun deleteRoutineById(id: Long)

    suspend fun deleteTaskById(id: Long)

    suspend fun updateRoutine(routine: RoutineEntity)

    suspend fun updateTask(task: TaskEntity)
}
