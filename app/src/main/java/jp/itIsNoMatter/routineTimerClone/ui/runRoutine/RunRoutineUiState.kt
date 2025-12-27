package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.TimerState

data class RunRoutineUiState(
    val routine: LoadedValue<Routine>,
    val timerState: TimerState,
    val currentTaskIndex: Int,
    val finished: Boolean = false,
) {
    val isEnabledPreviousButton: Boolean
        get() = currentTaskIndex > 0
    val isEnabledNextButton: Boolean
        get() = routine is LoadedValue.Done && currentTaskIndex < routine.value.tasks.size
}
