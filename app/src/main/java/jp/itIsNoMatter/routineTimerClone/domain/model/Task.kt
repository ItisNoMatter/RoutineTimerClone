package jp.itIsNoMatter.routineTimerClone.domain.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val duration: Duration,
    val announceRemainingTimeFlag: Boolean,
    val orderIndex: Int = 0,
) {
    constructor(
        id: String = UUID.randomUUID().toString(),
        name: String,
        minutes: Int,
        seconds: Int,
        announceRemainingTimeFlag: Boolean,
        orderIndex: Int = 0,
    ) : this(
        id = id,
        name = name,
        duration =
            Duration(
                minutes,
                seconds,
            ),
        announceRemainingTimeFlag = announceRemainingTimeFlag,
        orderIndex = orderIndex,
    )

    val minutes: Int
        get() = duration.minutes
    val seconds: Int
        get() = duration.seconds

    val isInvalidValue: Boolean
        get() = name.isEmpty() || duration == Duration.Zero

    companion object {
        val Empty =
            Task(
                id = "",
                name = "",
                duration = Duration(0, 0),
                announceRemainingTimeFlag = true,
            )
    }
}
