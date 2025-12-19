package jp.itIsNoMatter.routineTimerClone.ui.task.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import jp.itIsNoMatter.routineTimerClone.ui.task.TaskEditContent

@Composable
fun TaskEditScreen(
    navHostController: NavHostController,
    viewModel: TaskEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetch()
    }
    LaunchedEffect(Unit) {
        viewModel.navigateTo.collect { event ->
            when (event) {
                is NavEvent.NavigateBack -> {
                    navHostController.popBackStack()
                }

                is NavEvent.NavigateTo -> {
                    navHostController.navigate(event.route)
                }
            }
        }
    }

    TaskEditContent(
        taskTitle = uiState.taskTitle,
        taskDuration = uiState.taskDuration,
        announceFlag = uiState.announceFlag,
        onTaskNameChange = viewModel::onTaskTitleChange,
        onTaskMinuteChange = viewModel::onTaskDurationMinutesChange,
        onTaskSecondChange = viewModel::onTaskDurationSecondsChange,
        onToggleAnnounceRemainingTimeFlag = viewModel::onToggleAnnounceRemainingTime,
        onClickBackButton = viewModel::onClickBackButton,
    )
}
