package jp.itIsNoMatter.routineTimerClone.ui.task.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import jp.itIsNoMatter.routineTimerClone.ui.navigation.NavEvent

@Composable
fun TaskCreateScreen(
    navHostController: NavHostController,
    viewmodel: TaskCreateViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewmodel.create()
    }
    LaunchedEffect(Unit) {
        viewmodel.navigateTo.collect { event ->
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
    val uiState by viewmodel.uiState.collectAsState()
    TaskCreateContent(
        uiState,
        uiActions =
            TaskCreateUiActions(
                onClickBackButton = { viewmodel.onClickBackButton() },
                onTaskNameChange = viewmodel::onTaskTitleChange,
                onTaskMinuteChange = { minutes ->
                    viewmodel.onTaskDurationMinutesChange(
                        minutes,
                    )
                },
                onTaskSecondChange = { seconds ->
                    viewmodel.onTaskDurationSecondsChange(
                        seconds,
                    )
                },
                onToggleAnnounceRemainingTimeFlag = { task ->
                    viewmodel.onToggleAnnounceRemainingTime(
                        task,
                    )
                },
            ),
    )
}

@Composable
fun TaskCreateContent(
    uiState: TaskCreateUiState,
    uiActions: TaskCreateUiActions,
) {
    Scaffold(
        topBar = {
            TaskCreateTopBar(
                onClickBackButton = uiActions.onClickBackButton,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            Text(uiState.task.toString())
            TaskNameInput(
                taskTitle = uiState.taskTitle,
                onTaskNameChange = uiActions.onTaskNameChange,
            )
            TaskDurationInput(
                duration = uiState.taskDuration,
                onTaskMinutesChange = uiActions.onTaskMinuteChange,
                onTaskSecondChange = uiActions.onTaskSecondChange,
            )
        }
    }
}

@Composable
private fun TaskDurationInput(
    duration: Duration,
    onTaskMinutesChange: (Int) -> Unit,
    onTaskSecondChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            TextField(
                value = duration.minutes.toString(),
                onValueChange = { value ->
                    val newMinutes = value.toIntOrNull() ?: 0
                    onTaskMinutesChange(newMinutes)
                },
                modifier =
                    Modifier
                        .width(64.dp),
            )
            Text("分")
            TextField(
                value = duration.seconds.toString(),
                onValueChange = { value ->
                    val newSeconds = value.toIntOrNull() ?: 0
                    onTaskSecondChange(newSeconds)
                },
                modifier = Modifier.width(64.dp),
            )
            Text("秒")
        }
    }
}

@Composable
private fun TaskNameInput(
    taskTitle: String,
    onTaskNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Text(text = "作業の名前")
        TextField(
            value = taskTitle,
            onValueChange = onTaskNameChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
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
    TaskCreateContent(
        TaskCreateUiState(
            task =
                LoadedValue.Done(
                    Task(
                        id = 1,
                        name = "my task",
                        minutes = 1,
                        seconds = 30,
                        announceRemainingTimeFlag = true,
                    ),
                ),
        ),
        uiActions = TaskCreateUiActions.Noop,
    )
}
