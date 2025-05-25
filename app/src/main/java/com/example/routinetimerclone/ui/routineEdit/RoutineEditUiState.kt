package com.example.routinetimerclone.ui.routineEdit

import com.example.routinetimerclone.core.LoadedValue
import com.example.routinetimerclone.domain.model.Routine

data class RoutineEditUiState(
    val routine: LoadedValue<Routine>,
    val routineTitlePlaceHolder: String = "ルーチン名を入力",
)
