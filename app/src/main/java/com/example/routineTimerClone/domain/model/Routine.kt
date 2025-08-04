package com.example.routineTimerClone.domain.model

data class Routine(
    val id: Long,
    val name: String,
    var tasks: List<Task>,
) {
    fun getTotalDuration(): Duration {
        var totalseconds = 0
        for (task in tasks) {
            totalseconds += task.duration.getTotalSeconds()
        }
        return Duration(totalseconds / 60, totalseconds % 60)
    }

    fun isInitial(): Boolean {
        return tasks.isEmpty() && name.isEmpty()
    }

    fun taskSwap(
        from: Int,
        to: Int,
    ): Routine {
        val newTasks: MutableList<Task> = tasks.toMutableList()
        val task = newTasks.removeAt(from)
        newTasks.add(to, task)
        return Routine(id, name, newTasks)
    }

    companion object {
        val Empty = Routine(0, "", emptyList())
    }
}
