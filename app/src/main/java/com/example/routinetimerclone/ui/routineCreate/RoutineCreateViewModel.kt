package com.example.routinetimerclone.ui.routineCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinetimerclone.core.getOrNull
import com.example.routinetimerclone.data.repository.RoutineRepository
import com.example.routinetimerclone.domain.model.Routine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineCreateViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<RoutineCreateUiState>(RoutineCreateUiState.Loading)
        val uiState: StateFlow<RoutineCreateUiState> = _uiState.asStateFlow()

        fun create() {
            viewModelScope.launch {
                try {
                    val routineId = routineRepository.insertRoutine(Routine.Empty)

                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
                                RoutineCreateUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineCreateUiState.Error(e)
                    }
                }
            }
        }

        fun fetch(routineId: Long) {
            viewModelScope.launch {
                try {
                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
                                RoutineCreateUiState.Done(routine)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        RoutineCreateUiState.Error(e)
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
