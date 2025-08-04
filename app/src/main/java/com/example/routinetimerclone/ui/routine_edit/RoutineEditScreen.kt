package com.example.routinetimerclone.ui.routine_edit

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.routinetimerclone.ui.components.RoutineEditContent
import com.example.routinetimerclone.ui.navigation.NavEvent

@Composable
fun RoutineEditScreen(
    routineId: Long,
    navHostController: NavHostController,
    viewModel: RoutineEditViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.fetch(routineId)
    }
    LaunchedEffect(Unit) {
        viewModel.navigateTo.collect { event ->
            when (event) {
                is NavEvent.NavigateBack -> navHostController.popBackStack()
                is NavEvent.NavigateTo -> navHostController.navigate(event.route)
            }
        }
    }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is RoutineEditUiState.Loading -> {
            Text("Loading ...")
        }
        is RoutineEditUiState.Done -> {
            val doneRoutine = uiState.routine
            RoutineEditContent(
                routine = doneRoutine,
                onRoutineTitleChange = viewModel::onRoutineTitleChange,
                onClickAddButton = viewModel::onClickAddTaskButton,
                onClickTaskCard = viewModel::onClickTaskCard,
                onClickBackButton = viewModel::onClickBackButton,
            )
        }
        is RoutineEditUiState.Error -> {
            Log.e("RoutineEditScreen", "Error: ${uiState.e}")
        }
    }
}
