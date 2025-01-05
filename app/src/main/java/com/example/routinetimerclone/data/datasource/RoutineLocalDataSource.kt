package com.example.routinetimerclone.data.datasource

import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineLocalDataSource @Inject constructor(private val dao: RoutineDao) : RoutineDataSource {
    override fun getAllRoutines(): Flow<List<RoutineWithTasks>> {
        return dao.getAllRoutines()
    }

    override fun getRoutineById(id: Long): Flow<RoutineWithTasks?> {
        return dao.getRoutineById(id)
    }

    override fun getRoutineByName(name: String): Flow<RoutineWithTasks?> {
        return dao.getRoutineByName(name)
    }

    override fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>> {
        return dao.getRoutinesByName(name)
    }

    override suspend fun insertRoutine(routine: RoutineEntity): Long {
        return dao.insertRoutine(routine)
    }

    override suspend fun insertRoutines(routines: List<RoutineEntity>): List<Long> {
        return insertRoutines(routines)
    }

    override suspend fun insertTask(task: TaskEntity): Long {
        return dao.insertTask(task)
    }

    override suspend fun insertTasks(tasks: List<TaskEntity>): List<Long> {
        return tasks.map {
            insertTask(it)
        }
    }

    override fun getTasksByRoutineId(id: Long): Flow<List<TaskEntity>> {
        return dao.getTasksByRoutineId(id)
    }

    override fun getTaskById(id: Long): Flow<List<TaskEntity>> {
        return dao.getTaskById(id)
    }

    override suspend fun deleteAllTasksByRoutineId(id: Long) {
        dao.deleteAllTasksByRoutineId(id)
    }

    override suspend fun deleteRoutineById(id: Long) {
        dao.deleteRoutineById(id)
    }

    override suspend fun deleteTaskById(id: Long) {
        deleteTaskById(id)
    }

    override suspend fun updateRoutine(routine: RoutineEntity) {
        dao.updateRoutine(routine)
    }

    override suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }

    override suspend fun insertRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ): Long {
        return dao.insertRoutineWithTasks(routine, tasks)
    }

    override suspend fun updateRoutineWithTasks(
        routine: RoutineEntity,
        tasks: List<TaskEntity>,
    ) {
        dao.updateRoutineWithTasks(routine, tasks)
    }
}
