package com.example.routinetimerclone.data.repository

import com.example.routinetimerclone.data.datasource.RoutineDataSource
import com.example.routinetimerclone.data.entitiy.mapper.RoutineModelMapper
import com.example.routinetimerclone.data.entitiy.mapper.TaskModelMapper
import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl
    @Inject
    constructor(
        private val dataSource: RoutineDataSource,
        private val routineModelMapper: RoutineModelMapper,
        private val taskModelMapper: TaskModelMapper,
    ) : RoutineRepository {
        override fun getAllRoutines(): Flow<List<Routine>> {
            return dataSource.getAllRoutines().map { routineWithTasks ->
                routineWithTasks.map { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override fun getRoutine(id: Long): Flow<Routine?> {
            return dataSource.getRoutineById(id).map { routineWithTasks ->
                routineWithTasks?.let { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override fun getRoutineByName(name: String): Flow<Routine?> {
            return dataSource.getRoutineByName(name).map { routineWithTasks ->
                routineWithTasks?.let { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override suspend fun insertRoutine(routine: Routine): Long {
            val entity = routineModelMapper.toEntity(routine)
            val routineId =
                dataSource.insertRoutineWithTasks(
                    entity.routine,
                    entity.tasks,
                )
            return routineId
        }

        override suspend fun insertRoutines(routines: List<Routine>): List<Long> {
            return routines.map {
                val entitiy = routineModelMapper.toEntity(it)
                dataSource.insertRoutineWithTasks(entitiy.routine, entitiy.tasks)
            }
        }

        override suspend fun updateRoutine(routine: Routine) {
            val entity = routineModelMapper.toEntity(routine)
            dataSource.updateRoutineWithTasks(entity.routine, entity.tasks)
        }

        override suspend fun deleteRoutineById(id: Long) {
            dataSource.deleteRoutineById(id)
        }

        override fun getTasksByRoutineId(routineId: Long): Flow<List<Task>> {
            return dataSource.getTasksByRoutineId(routineId).map { taskEntities ->
                taskEntities.map { taskModelMapper.toDomain(it) }
            }
        }

        override fun getTaskByTaskId(id: Long): Flow<Task?> {
            return dataSource.getTaskByTaskId(id).map { taskEntities ->
                taskEntities.let { taskModelMapper.toDomain(it) }
            }
        }

        override suspend fun insertTask(
            task: Task,
            parentRoutineId: Long,
        ): Long {
            return dataSource.insertTask(taskModelMapper.toEntity(task, parentRoutineId))
        }

        override suspend fun insertTasks(
            tasks: List<Task>,
            parentRoutineId: Long,
        ): List<Long> {
            return dataSource.insertTasks(
                tasks.map { taskModelMapper.toEntity(it, parentRoutineId) },
            )
        }

        override suspend fun updateTask(
            task: Task,
            parentRoutineId: Long,
        ) {
            dataSource.updateTask(taskModelMapper.toEntity(task, parentRoutineId))
        }

        override suspend fun deleteTaskById(id: Long) {
            dataSource.deleteTaskById(id)
        }

        override suspend fun deleteAllTasksByRoutineId(routineId: Long) {
            dataSource.deleteAllTasksByRoutineId(routineId)
        }

        override fun getRoutinesByName(name: String): Flow<List<Routine>> {
            return dataSource.getRoutinesByName(name).map { routinesWithTasks ->
                routinesWithTasks.map { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }
    }
