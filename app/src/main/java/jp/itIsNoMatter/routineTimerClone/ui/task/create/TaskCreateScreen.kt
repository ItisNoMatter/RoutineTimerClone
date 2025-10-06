package jp.itIsNoMatter.routineTimerClone.ui.task.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
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
                onTaskMinuteChange = { task, minute ->
                    viewmodel.onTaskDurationMinutesChange(
                        task,
                        minute,
                    )
                },
                onTaskSecondChange = { task, second ->
                    viewmodel.onTaskDurationSecondsChange(
                        task,
                        second,
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
            Text(text = "作業の名前")
            TextField(
                value = uiState.taskTitle,
                onValueChange = uiActions.onTaskNameChange,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateTopBar(
    onClickBackButton: () -> Unit = {},
) {
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
            showDurationInputDialog = true,
        ),
        uiActions = TaskCreateUiActions.Noop,
    )
}
