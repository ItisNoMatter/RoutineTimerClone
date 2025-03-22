package com.example.routinetimerclone.ui.routineList

import androidx.lifecycle.ViewModel
import com.example.routinetimerclone.data.repository.FakeRoutineRepository
import com.example.routinetimerclone.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel
    @Inject
    constructor(
        private val uiAction: RoutineListUiAction,
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        val routines = routineRepository.getAllRoutines()

        fun onPlayRoutineClick(routineId: Long) {
            uiAction.onPlayButtonClick(routineId)
        }

        fun onAddRoutineClick() {
            uiAction.onAddButtonClick()
        }

        companion object {
            val Noop =
                RoutineListViewModel(
                    RoutineListUiAction.Noop,
                    FakeRoutineRepository,
                )
        }
    }
