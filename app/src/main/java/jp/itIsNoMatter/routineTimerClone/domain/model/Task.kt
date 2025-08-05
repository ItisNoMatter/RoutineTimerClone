package jp.itIsNoMatter.routineTimerClone.domain.model

data class Task(
    val id: Long,
    val name: String,
    val duration: Duration,
    val announceRemainingTimeFlag: Boolean = true,
) {
    constructor(
        id: Long,
        name: String,
        minutes: Int,
        seconds: Int,
    ) : this(id, name, Duration(minutes, seconds))

    val minutes: Int
        get() = duration.minutes
    val seconds: Int
        get() = duration.seconds

    companion object {
        val Empty = Task(0, "", Duration(0, 0))
    }
}
