package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunRoutineViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val routineId: Long = savedStateHandle.toRoute<Route.RunRoutine>().routineId

        private val _uiState: MutableStateFlow<RunRoutineUiState> =
            MutableStateFlow(
                RunRoutineUiState(
                    routine = LoadedValue.Loading,
                ),
            )
        val uiState = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                routineRepository.getRoutine(routineId).collect { routine ->
                    _uiState.update {
                        it.copy(routine = routine)
                    }
                }
            }
        }
    }
