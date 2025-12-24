package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

data class RunRoutineUiState(
    val routine: LoadedValue<Routine>,
)
