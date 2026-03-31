package jp.itIsNoMatter.routineTimerClone.data.repository

import android.util.Log
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.local.datasource.RoutineLocalDataSource
import jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import jp.itIsNoMatter.routineTimerClone.data.remote.datasource.RoutineRemoteDataSource
import jp.itIsNoMatter.routineTimerClone.data.remote.toEntity
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl
    @Inject
    constructor(
        private val localDataSource: RoutineLocalDataSource,
        private val remoteDataSource: RoutineRemoteDataSource,
        private val routineModelMapper: RoutineModelMapper,
        private val taskModelMapper: TaskModelMapper,
    ) : RoutineRepository {
        override fun getAllRoutines(): Flow<List<Routine>> {
            return localDataSource.getAllRoutines().map { routineWithTasks ->
                routineWithTasks.map { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override fun getRoutine(id: String): Flow<LoadedValue<Routine>> {
            return localDataSource.getRoutineById(id).map { it ->
                if (it != null) {
                    LoadedValue.Done(routineModelMapper.toDomain(it.routine, it.tasks))
                } else {
                    LoadedValue.Error(Exception("Routine not found"))
                }
            }
        }

        override fun getRoutineByName(name: String): Flow<Routine?> {
            return localDataSource.getRoutineByName(name).map { routineWithTasks ->
                routineWithTasks?.let { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override suspend fun insertRoutine(routine: Routine) {
            val entity = routineModelMapper.toEntity(routine)
            localDataSource.insertRoutineWithTasks(entity.routine, entity.tasks)

            val response = routineModelMapper.toResponse(routine)

            try {
                remoteDataSource.addRoutine(response)
            } catch (e: Exception) {
            }
        }

        override suspend fun insertRoutines(routines: List<Routine>) {
            routines.forEach {
                val entity = routineModelMapper.toEntity(it)
                localDataSource.insertRoutineWithTasks(entity.routine, entity.tasks)
            }
        }

        override suspend fun updateRoutine(routine: Routine) {
            val entity = routineModelMapper.toEntity(routine)
            localDataSource.updateRoutineWithTasks(entity.routine, entity.tasks)

            val response = routineModelMapper.toResponse(routine)
            try {
                remoteDataSource.updateRoutine(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override suspend fun deleteRoutineById(id: String) {
            localDataSource.deleteRoutineById(id)

            try {
                remoteDataSource.deleteRoutine(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun getTasksByRoutineId(routineId: String): Flow<List<Task>> {
            return localDataSource.getTasksByRoutineId(routineId).map { taskEntities ->
                taskEntities.map { taskModelMapper.toDomain(it) }
            }
        }

        override fun getTaskByTaskId(id: String): Flow<Task?> {
            return localDataSource.getTaskByTaskId(id).map { taskEntities ->
                taskEntities.let { taskModelMapper.toDomain(it) }
            }
        }

        override suspend fun insertTask(
            task: Task,
            parentRoutineId: String,
        ) {
            val currentTasks = localDataSource.getTasksByRoutineId(parentRoutineId).first()
            val nextIndex = (currentTasks.maxOfOrNull { it.orderIndex } ?: -1) + 1
            val taskWithIndex = task.copy(orderIndex = nextIndex)
            localDataSource.insertTask(taskModelMapper.toEntity(taskWithIndex, parentRoutineId))
        }

        override suspend fun insertTasks(
            tasks: List<Task>,
            parentRoutineId: String,
        ) {
            localDataSource.insertTasks(
                tasks.map { taskModelMapper.toEntity(it, parentRoutineId) },
            )
        }

        override suspend fun updateTask(
            task: Task,
            parentRoutineId: String,
        ) {
            localDataSource.updateTask(taskModelMapper.toEntity(task, parentRoutineId))
        }

        override suspend fun deleteTaskById(id: String) {
            localDataSource.deleteTaskById(id)
        }

        override suspend fun deleteAllTasksByRoutineId(routineId: String) {
            localDataSource.deleteAllTasksByRoutineId(routineId)
        }

        override fun getRoutinesByName(name: String): Flow<List<Routine>> {
            return localDataSource.getRoutinesByName(name).map { routinesWithTasks ->
                routinesWithTasks.map { routineModelMapper.toDomain(it.routine, it.tasks) }
            }
        }

        override suspend fun syncRoutines() {
            try {
                val remoteData = remoteDataSource.getRoutines()

                val routineEntities = remoteData.map { it.toEntity() }

                val taskEntities =
                    remoteData.flatMap { routine ->
                        routine.tasks.map { task -> task.toEntity(routine.id) }
                    }

                localDataSource.insertRoutines(routineEntities)
                localDataSource.insertTasks(taskEntities)
            } catch (e: Exception) {
                // サーバーが落ちている、電波がない等のエラー時はここでキャッチ
                e.printStackTrace()
                // ※エラーが起きても、ViewModelはRoomの古いデータを表示し続けるのでアプリは落ちません（超安全！）
            }
        }

        override suspend fun addRoutine(routine: RoutineResponse) {
            // サーバー（Remote）にデータを送信！
            remoteDataSource.addRoutine(routine)
        }
    }
