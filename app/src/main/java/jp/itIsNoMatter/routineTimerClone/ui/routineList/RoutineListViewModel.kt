package jp.itIsNoMatter.routineTimerClone.ui.routineList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.data.repository.FakeRoutineRepository
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import kotlinx.coroutines.launch
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

        fun deleteRoutine(routineId: Long) {
            viewModelScope.launch {
                routineRepository.deleteRoutineById(routineId)
            }
        }
    }
