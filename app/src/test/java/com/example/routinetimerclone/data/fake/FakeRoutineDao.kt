package com.example.routinetimerclone.data.fake

import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import com.example.routinetimerclone.data.entitiy.RoutineWithTasks
import com.example.routinetimerclone.data.entitiy.TaskEntity

class FakeRoutineDao : RoutineDao {
    private val routines = mutableMapOf<Int, RoutineEntity>()
    private val tasks = mutableMapOf<Int, MutableList<TaskEntity>>()

    private var autoIncrementRoutineId = 1
    private var autoIncrementTaskId = 1

    override suspend fun getAllRoutines(): List<RoutineWithTasks> {
        return routines.values.map { routine ->
            val routineId = routine.id
            val taskList = tasks[routineId] ?: emptyList()
            RoutineWithTasks(routine, taskList)
        }
    }

    override suspend fun getRoutineById(id: Int): RoutineWithTasks? {
        val routine = routines[id] ?: return null
        val taskList = tasks[id] ?: emptyList()
        return RoutineWithTasks(routine, taskList)
    }

    override suspend fun getRoutineByName(name: String): RoutineWithTasks? {
        val routine = routines.values.find { it.name == name } ?: return null
        val taskList = tasks[routine.id] ?: emptyList()
        return RoutineWithTasks(routine, taskList)
    }

    override suspend fun getRoutinesByName(name: String): List<RoutineWithTasks> {
        val routines = routines.values.filter { it.name.contains(name) }
        return routines.map { routine ->
            val taskList = tasks[routine.id] ?: emptyList()
            RoutineWithTasks(routine, taskList)
        }
    }

    override suspend fun insertRoutine(routine: RoutineEntity): Int {
        val newId =
            if (routine.id == 0) {
                autoIncrementRoutineId++
            } else {
                routine.id
            }
        routines[newId] = RoutineEntity(newId, routine.name)
        tasks[newId] = mutableListOf()
        return newId
    }

    override suspend fun insertRoutines(routines: List<RoutineEntity>) {
        for (routine in routines) {
            insertRoutine(routine)
        }
    }

    override suspend fun insertTasks(
        tasks: List<TaskEntity>,
        parentRoutineId: Int,
    ) {
        if (!routines.containsKey(parentRoutineId)) {
            throw IllegalArgumentException("Parent routine with id $parentRoutineId does not exist")
        }
        if (!this.tasks.containsKey(parentRoutineId)) {
            this.tasks[parentRoutineId] = mutableListOf()
        }
        for (task in tasks) {
            val newId =
                if (task.id == 0) {
                    autoIncrementTaskId++
                } else {
                    task.id
                }
            val updatedTask = TaskEntity(newId, task.name, task.seconds, parentRoutineId)
            this.tasks[parentRoutineId]?.add(updatedTask)
        }
    }

    override suspend fun getTasksByRoutineId(id: Int): List<TaskEntity> {
        return tasks[id] ?: emptyList()
    }

    override suspend fun deleteTasksByRoutineId(id: Int) {
        tasks.remove(id)
    }

    override suspend fun deleteRoutineById(id: Int) {
        routines.remove(id)
        deleteTasksByRoutineId(id)
    }

    override suspend fun deleteTaskById(id: Int) {
        for (taskList in tasks.values) {
            taskList.removeIf { it.id == id }
        }
    }

    override suspend fun updateRoutine(routine: RoutineEntity) {
        if (routine.id == 0) {
            insertRoutine(routine)
            return
        }
        if (!routines.containsKey(routine.id)) {
            insertRoutine(routine)
            return
        }
        routines[routine.id] = routine
    }

    override suspend fun updateTask(task: TaskEntity) {
        if (task.id == 0) {
            insertTasks(listOf(task), task.parentRoutineId)
            return
        }

        if (!tasks.containsKey(task.parentRoutineId))
            {
                throw IllegalArgumentException("Parent routine with id ${task.parentRoutineId} does not exist")
            }

        val taskList = tasks[task.parentRoutineId] ?: return

        if (taskList.indexOfFirst { it.id == task.id } == -1) {
            throw IllegalArgumentException("Task with id ${task.id} does not exist")
        }
        taskList[taskList.indexOfFirst { it.id == task.id }] = task
    }
}
