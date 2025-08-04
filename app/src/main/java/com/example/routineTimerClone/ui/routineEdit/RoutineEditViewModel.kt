package com.example.routineTimerClone.ui.routineEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routineTimerClone.core.getOrNull
import com.example.routineTimerClone.data.repository.RoutineRepository
import com.example.routineTimerClone.ui.navigation.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
        private val _navigateTo = MutableSharedFlow<NavEvent>()
        val navigateTo = _navigateTo.asSharedFlow()

        fun fetch(routineId: Long) {
            viewModelScope.launch {
                try {
                    routineRepository.getRoutine(routineId).collect { value ->
                        val routine = value.getOrNull()
                        if (routine != null) {
                            _uiState.update {
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
            val state = uiState.value
            if (state !is RoutineEditUiState.Done) return
            viewModelScope.launch {
                routineRepository.updateRoutine(state.routine.copy(name = title))
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

        fun onClickBackButton() {
            viewModelScope.launch {
                _navigateTo.emit(NavEvent.NavigateBack)
            }
        }
    }
