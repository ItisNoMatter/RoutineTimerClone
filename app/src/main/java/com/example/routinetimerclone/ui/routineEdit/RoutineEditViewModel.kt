package com.example.routinetimerclone.ui.routineEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinetimerclone.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineEditViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        private var routineId: Long = -1

        fun setRoutineId(id: Long) {
            routineId = id
        }

        val routine = routineRepository.getRoutine(routineId)

        fun onRoutineTitleChange(title: String) {
            viewModelScope.launch {
            }
        }

        fun onClickAddTaskButton() {
            viewModelScope.launch {
            }
        }

        fun onClickTaskCard() {
            viewModelScope.launch {
            }
        }
    }
