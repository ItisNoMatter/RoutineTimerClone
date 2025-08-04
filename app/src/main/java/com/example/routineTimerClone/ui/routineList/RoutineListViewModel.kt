package com.example.routineTimerClone.ui.routineList

import androidx.lifecycle.ViewModel
import com.example.routineTimerClone.data.repository.FakeRoutineRepository
import com.example.routineTimerClone.data.repository.RoutineRepository
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
