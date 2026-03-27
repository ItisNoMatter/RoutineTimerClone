package jp.itIsNoMatter.routineTimerClone.data.repository

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineDataSource
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
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

        override fun getRoutine(id: String): Flow<LoadedValue<Routine>> {
            return dataSource.getRoutineById(id).map { it ->
                if (it != null) {
                    LoadedValue.Done(routineModelMapper.toDomain(it.routine, it.tasks))
                } else {
                    LoadedValue.Error(Exception("Routine not found"))
                }
            }
        }

        override fun getRoutineByName(name: String): Flow<Routine?> {
            return dataSource.getRoutineByName(name).map { routineWithTasks ->
                routineWithTasks?.let { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override suspend fun insertRoutine(routine: Routine) {
            val entity = routineModelMapper.toEntity(routine)
            dataSource.insertRoutineWithTasks(entity.routine, entity.tasks)
        }

        override suspend fun insertRoutines(routines: List<Routine>) {
            routines.forEach {
                val entity = routineModelMapper.toEntity(it)
                dataSource.insertRoutineWithTasks(entity.routine, entity.tasks)
            }
        }

        override suspend fun updateRoutine(routine: Routine) {
            val entity = routineModelMapper.toEntity(routine)
            dataSource.updateRoutineWithTasks(entity.routine, entity.tasks)
        }

        override suspend fun deleteRoutineById(id: String) {
            dataSource.deleteRoutineById(id)
        }

        override fun getTasksByRoutineId(routineId: String): Flow<List<Task>> {
            return dataSource.getTasksByRoutineId(routineId).map { taskEntities ->
                taskEntities.map { taskModelMapper.toDomain(it) }
            }
        }

        override fun getTaskByTaskId(id: String): Flow<Task?> {
            return dataSource.getTaskByTaskId(id).map { taskEntities ->
                taskEntities.let { taskModelMapper.toDomain(it) }
            }
        }

        override suspend fun insertTask(
            task: Task,
            parentRoutineId: String,
        ) {
            dataSource.insertTask(taskModelMapper.toEntity(task, parentRoutineId))
        }

        override suspend fun insertTasks(
            tasks: List<Task>,
            parentRoutineId: String,
        ) {
            dataSource.insertTasks(
                tasks.map { taskModelMapper.toEntity(it, parentRoutineId) },
            )
        }

        override suspend fun updateTask(
            task: Task,
            parentRoutineId: String,
        ) {
            dataSource.updateTask(taskModelMapper.toEntity(task, parentRoutineId))
        }

        override suspend fun deleteTaskById(id: String) {
            dataSource.deleteTaskById(id)
        }

        override suspend fun deleteAllTasksByRoutineId(routineId: String) {
            dataSource.deleteAllTasksByRoutineId(routineId)
        }

        override fun getRoutinesByName(name: String): Flow<List<Routine>> {
            return dataSource.getRoutinesByName(name).map { routinesWithTasks ->
                routinesWithTasks.map { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }
    }
