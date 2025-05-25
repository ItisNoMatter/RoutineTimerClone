package com.example.routinetimerclone.ui.routineEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinetimerclone.core.ext.require
import com.example.routinetimerclone.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineEditViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val routineRepository: RoutineRepository,
) : ViewModel() {
    private val _routineId = savedStateHandle.require<Long>("routineId")

    val routine = routineRepository.getRoutine(_routineId)

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