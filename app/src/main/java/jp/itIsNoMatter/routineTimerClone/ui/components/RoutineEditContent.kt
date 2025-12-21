package jp.itIsNoMatter.routineTimerClone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.itIsNoMatter.routineTimerClone.R
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

@Composable
fun RoutineEditContent(
    routine: Routine,
    onRoutineTitleChange: (String) -> Unit = {},
    onClickAddButton: () -> Unit = {},
    onClickTaskCard: (taskId: Long) -> Unit = {},
    onClickBackButton: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            RoutineEditTopBar(
                routine = routine,
                onRoutineTitleChange = onRoutineTitleChange,
                onClickBackButton = onClickBackButton,
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
                    TaskCard(routine.tasks[index], NodePosition.FIRST, onClick = { onClickTaskCard(routine.tasks[index].id) })
                } else if (index == routine.tasks.size - 1) {
                    TaskCard(routine.tasks[index], NodePosition.LAST, onClick = { onClickTaskCard(routine.tasks[index].id) })
                } else {
                    TaskCard(routine.tasks[index], NodePosition.MIDDLE, onClick = { onClickTaskCard(routine.tasks[index].id) })
                }
            }
        }
    }
}

@Composable
fun RoutineEditTopBar(
    routine: Routine,
    onRoutineTitleChange: (String) -> Unit = {},
    onClickBackButton: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(104.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .statusBarsPadding(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onClickBackButton()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Box(modifier = Modifier.weight(1f)) {
                if (routine.name.isEmpty()) {
                    Text(
                        text = stringResource(R.string.routine_edit_title_place_holder),
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
                            announceRemainingTimeFlag = true,
                        ),
                        Task(
                            id = 2,
                            name = "test",
                            duration = Duration(minutes = 3, seconds = 15),
                            announceRemainingTimeFlag = true,
                        ),
                        Task(
                            id = 3,
                            name = "test",
                            duration = Duration(minutes = 4, seconds = 0),
                            announceRemainingTimeFlag = true,
                        ),
                    ),
            ),
    )
}
