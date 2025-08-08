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
        announceRemainingTimeFlag: Boolean = true,
    ) : this(
        id = id,
        name = name,
        duration =
            Duration(
                minutes,
                seconds,
            ),
        announceRemainingTimeFlag = announceRemainingTimeFlag,
    )

    val minutes: Int
        get() = duration.minutes
    val seconds: Int
        get() = duration.seconds

    companion object {
        val Empty =
            Task(
                id = 0,
                name = "",
                duration = Duration(0, 0),
                announceRemainingTimeFlag = false,
            )
    }
}
