package com.example.routinetimerclone.data.repository

import com.example.routinetimerclone.data.datasource.RoutineLocalDataSource
import com.example.routinetimerclone.data.entitiy.mapper.RoutineModelMapper
import com.example.routinetimerclone.data.entitiy.mapper.TaskModelMapper
import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoutineRepositoryImpl(
    private val localDataSource: RoutineLocalDataSource,
) : RoutineRepository {
    override fun getAllRoutines(): Flow<List<Routine>> {
        return localDataSource.getAllRoutines().map { routineWithTasks ->
            routineWithTasks.map { RoutineModelMapper.toDomain(it.routine, it.tasks) }
        }
    }

    override fun getRoutineById(id: Long): Flow<Routine?> {
        return localDataSource.getRoutineById(id).map { routineWithTasks ->
            routineWithTasks?.let { RoutineModelMapper.toDomain(it.routine, it.tasks) }
        }
    }

    override fun getRoutineByName(name: String): Flow<Routine?> {
        return localDataSource.getRoutineByName(name).map { routineWithTasks ->
            routineWithTasks?.let { RoutineModelMapper.toDomain(it.routine, it.tasks) }
        }
    }

    override suspend fun insertRoutine(routine: Routine): Long {
        val routineId = localDataSource.insertRoutine(RoutineModelMapper.toEntity(routine).routine)
        localDataSource.insertTasks(RoutineModelMapper.toEntity(routine).tasks.map { it.copy(parentRoutineId = routineId) })
        return routineId
    }

    override suspend fun insertRoutines(routines: List<Routine>): List<Long> {
        return routines.map {
            val parentid = localDataSource.insertRoutine(RoutineModelMapper.toEntity(it).routine)
            localDataSource.insertTasks(RoutineModelMapper.toEntity(it).tasks.map { it.copy(parentRoutineId = parentid) })
            parentid
        }
    }

    override suspend fun updateRoutine(routine: Routine) {
        updateRoutine(routine)
    }

    override suspend fun deleteRoutineById(id: Long) {
        localDataSource.deleteRoutineById(id)
    }

    override fun getTasksByRoutineId(routineId: Long): Flow<List<Task>> {
        return localDataSource.getTasksByRoutineId(routineId).map { taskEntities ->
            taskEntities.map { TaskModelMapper.toDomain(it) }
        }
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return localDataSource.getTaskById(id).map { taskEntities ->
            taskEntities.firstOrNull()?.let { TaskModelMapper.toDomain(it) }
        }
    }

    override suspend fun insertTask(
        task: Task,
        parentRoutineId: Long,
    ): Long {
        return localDataSource.insertTask(TaskModelMapper.toEntity(task, parentRoutineId))
    }

    override suspend fun insertTasks(
        tasks: List<Task>,
        parentRoutineId: Long,
    ): List<Long> {
        return localDataSource.insertTasks(tasks.map { TaskModelMapper.toEntity(it, parentRoutineId) })
    }

    override suspend fun updateTask(
        task: Task,
        parentRoutineId: Long,
    ) {
        localDataSource.updateTask(TaskModelMapper.toEntity(task, parentRoutineId))
    }

    override suspend fun deleteTaskById(id: Long) {
        localDataSource.deleteTaskById(id)
    }

    override suspend fun deleteAllTasksByRoutineId(routineId: Long) {
        localDataSource.deleteRoutineById(routineId)
    }

    override fun getRoutinesByName(name: String): Flow<List<Routine>> {
        return localDataSource.getRoutinesByName(name).map { routinesWithTasks ->
            routinesWithTasks.map { RoutineModelMapper.toDomain(it.routine, it.tasks) }
        }
    }
}
