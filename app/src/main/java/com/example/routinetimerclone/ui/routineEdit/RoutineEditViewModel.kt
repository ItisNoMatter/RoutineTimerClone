package com.example.routinetimerclone.ui.routineEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinetimerclone.core.getOrNull
import com.example.routinetimerclone.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineEditViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<RoutineEditUiState>(RoutineEditUiState.Loading)
        val uiState: StateFlow<RoutineEditUiState> = _uiState.asStateFlow()

        fun fetch(routineId: Long) {
            viewModelScope.launch {
                try {
                    routineRepository.getRoutine(routineId).collect { value ->
                        _uiState.update {
                            val routine = value.getOrNull()
                            if (routine == null) {
                                RoutineEditUiState.New
                            } else {
                                RoutineEditUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineEditUiState.Error(e)
                    }
                }
            }
        }

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
