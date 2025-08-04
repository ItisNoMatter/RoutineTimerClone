package com.example.routinetimerclone.ui.routine_list

import androidx.lifecycle.ViewModel
import com.example.routinetimerclone.data.repository.FakeRoutineRepository
import com.example.routinetimerclone.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        val routines = routineRepository.getAllRoutines()

        companion object {
            val Noop =
                RoutineListViewModel(
                    routineRepository = FakeRoutineRepository,
                )
        }
    }
