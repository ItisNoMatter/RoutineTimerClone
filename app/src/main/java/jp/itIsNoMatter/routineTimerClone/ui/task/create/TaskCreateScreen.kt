package jp.itIsNoMatter.routineTimerClone.ui.task.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent
import jp.itIsNoMatter.routineTimerClone.ui.task.TaskEditContent

@Composable
fun TaskCreateScreen(
    navHostController: NavHostController,
    viewModel: TaskCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.create()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateTopBar(onClickBackButton: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        TopAppBar(
            title = {
                Text("作業と所要時間")
            },
            navigationIcon = {
                IconButton(onClick = onClickBackButton) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            },
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun TaskCreateContentPreview() {
    TaskEditContent(
        taskTitle = "Task Title",
        taskDuration = Duration.Zero,
        announceFlag = true,
        onTaskNameChange = {},
        onTaskMinuteChange = {},
        onTaskSecondChange = {},
        onToggleAnnounceRemainingTimeFlag = {},
        onClickBackButton = {},
    )
}
