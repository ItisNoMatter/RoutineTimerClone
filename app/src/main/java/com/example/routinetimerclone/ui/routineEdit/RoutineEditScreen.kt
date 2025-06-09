package com.example.routinetimerclone.ui.routineEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.routinetimerclone.core.LoadedValue
import com.example.routinetimerclone.domain.model.Duration
import com.example.routinetimerclone.domain.model.Routine
import com.example.routinetimerclone.domain.model.Task

@Composable
fun RoutineEditScreen(
    routineId: Long,
    navHostController: NavHostController,
    viewModel: RoutineEditViewModel = hiltViewModel(),
) {
    val routine by viewModel.routine.collectAsState(
        initial = LoadedValue.Loading,
    )
    when (routine) {
        is LoadedValue.Done -> {
            val doneRoutine = (routine as LoadedValue.Done<Routine>).value
            RoutineEditContent(
                routine = doneRoutine,
                onRoutineTitleChange = viewModel::onRoutineTitleChange,
                onClickAddButton = viewModel::onClickAddTaskButton,
                onClickTaskCard = viewModel::onClickTaskCard,
            )
        }

        is LoadedValue.Error -> {}
        is LoadedValue.Loading -> {}
    }
}

@Composable
fun RoutineEditContent(
    routine: Routine,
    routineTitlePlaceHolder: String = "ルーチン名を入力",
    onRoutineTitleChange: (String) -> Unit = {},
    onClickAddButton: () -> Unit = {},
    onClickTaskCard: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            RoutineEditTopBar(
                routine = routine,
                routineTitlePlaceHolder = routineTitlePlaceHolder,
                onRoutineTitleChange = onRoutineTitleChange,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAddButton) {
                Icon(Icons.Filled.Add, "Add Task")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            items(routine.tasks.size) { index ->
                if (index == 0) {
                    TaskCard(routine.tasks[index], NodePosition.FIRST)
                } else if (index == routine.tasks.size - 1) {
                    TaskCard(routine.tasks[index], NodePosition.LAST)
                } else {
                    TaskCard(routine.tasks[index], NodePosition.MIDDLE)
                }
            }
        }
    }
}

@Composable
fun RoutineEditTopBar(
    routine: Routine,
    routineTitlePlaceHolder: String = "ルーチン名を入力",
    onRoutineTitleChange: (String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(104.dp)
                .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Box {
                if (routine.name.isEmpty()) {
                    Text(
                        text = routineTitlePlaceHolder,
                    )
                }
                BasicTextField(
                    value = routine.name,
                    onValueChange = onRoutineTitleChange,
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }
        }
        Text(
            text = "計 " + routine.getTotalDuration().toDisplayString(),
            modifier =
                Modifier
                    .padding(bottom = 8.dp, end = 32.dp)
                    .align(Alignment.BottomEnd),
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun RoutineEditContentPreview() {
    RoutineEditContent(
        routine =
            Routine(
                id = 1,
                name = "test",
                tasks =
                    listOf(
                        Task(
                            id = 1,
                            name = "test",
                            duration = Duration(minutes = 1, seconds = 2),
                        ),
                        Task(
                            id = 2,
                            name = "test",
                            duration = Duration(minutes = 3, seconds = 15),
                        ),
                        Task(
                            id = 3,
                            name = "test",
                            duration = Duration(minutes = 4, seconds = 0),
                        ),
                    ),
            ),
    )
}
