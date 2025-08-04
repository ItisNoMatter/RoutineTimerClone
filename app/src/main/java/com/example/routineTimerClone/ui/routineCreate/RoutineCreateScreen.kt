package com.example.routineTimerClone.ui.routineCreate

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.routineTimerClone.ui.components.RoutineEditContent
import com.example.routineTimerClone.ui.navigation.NavEvent

@Composable
fun RoutineCreateScreen(
    navHostController: NavHostController,
    viewModel: RoutineCreateViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.create()
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
        is RoutineCreateUiState.Loading -> {
            Text("Loading ...")
        }
        is RoutineCreateUiState.Done -> {
            val doneRoutine = uiState.routine
            RoutineEditContent(
                routine = doneRoutine,
                onRoutineTitleChange = viewModel::onRoutineTitleChange,
                onClickAddButton = viewModel::onClickAddTaskButton,
                onClickTaskCard = viewModel::onClickTaskCard,
                onClickBackButton = viewModel::onClickBackButton,
            )
        }
        is RoutineCreateUiState.Error -> {
            Log.e("RoutineCreateScreen", "Error: ${uiState.e}")
        }
    }
}
