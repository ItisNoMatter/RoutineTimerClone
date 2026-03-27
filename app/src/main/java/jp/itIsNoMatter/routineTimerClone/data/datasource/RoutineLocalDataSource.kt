package jp.itIsNoMatter.routineTimerClone.data.datasource

import jp.itIsNoMatter.routineTimerClone.data.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineLocalDataSource
    @Inject
    constructor(private val dao: RoutineDao) : RoutineDataSource {
        override fun getAllRoutines(): Flow<List<RoutineWithTasks>> {
            return dao.getAllRoutines()
        }

        override fun getRoutineById(id: String): Flow<RoutineWithTasks?> {
            return dao.getRoutineById(id)
        }

        override fun getRoutineByName(name: String): Flow<RoutineWithTasks?> {
            return dao.getRoutineByName(name)
        }

        override fun getRoutinesByName(name: String): Flow<List<RoutineWithTasks>> {
            return dao.getRoutinesByName(name)
        }

        override suspend fun insertRoutine(routine: RoutineEntity) {
            dao.insertRoutine(routine)
        }

        override suspend fun insertRoutines(routines: List<RoutineEntity>) {
            return dao.insertRoutines(routines)
        }

        override suspend fun insertTask(task: TaskEntity) {
            return dao.insertTask(task)
        }

        override suspend fun insertTasks(tasks: List<TaskEntity>) {
            tasks.forEach {
                insertTask(it)
            }
        }

        override fun getTasksByRoutineId(id: String): Flow<List<TaskEntity>> {
            return dao.getTasksByRoutineId(id)
        }

        override fun getTaskByTaskId(id: String): Flow<TaskEntity> {
            return dao.getTaskByTaskId(id)
        }

        override suspend fun deleteAllTasksByRoutineId(id: String) {
            dao.deleteAllTasksByRoutineId(id)
        }

        override suspend fun deleteRoutineById(id: String) {
            dao.deleteRoutineById(id)
        }

        override suspend fun deleteTaskById(id: String) {
            dao.deleteTaskById(id)
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
        ): String {
            return dao.insertRoutineWithTasks(routine, tasks)
        }

        override suspend fun updateRoutineWithTasks(
            routine: RoutineEntity,
            tasks: List<TaskEntity>,
        ) {
            dao.updateRoutineWithTasks(routine, tasks)
        }
    }
