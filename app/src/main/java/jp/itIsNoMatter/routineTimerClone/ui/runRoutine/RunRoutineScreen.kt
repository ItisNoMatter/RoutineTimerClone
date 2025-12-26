package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

@Composable
fun runRoutineScreen(
    viewModel: RunRoutineViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    val routine = uiState.routine
    if (routine is LoadedValue.Done) {
        runRoutineContent(
            routine = routine.value,
            remainSeconds = uiState.timerState.remainingDuration.getTotalSeconds(),
            onClickNext = viewModel::onClickNext,
            onClickPrevious = viewModel::onClickPrevious,
            onClickPlay = viewModel::onClickPlay,
            isEnabledPreviousButton = uiState.isEnabledPreviousButton,
            isEnabledNextButton = uiState.isEnabledNextButton,
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("Loading ...")
        }
    }
}

@Composable
fun runRoutineContent(
    routine: Routine,
    remainSeconds: Int,
    onClickNext: () -> Unit = {},
    onClickPrevious: () -> Unit = {},
    onClickPlay: () -> Unit = {},
    isEnabledPreviousButton: Boolean,
    isEnabledNextButton: Boolean,
) {
    Scaffold(
        topBar = {
            RunRoutineTopBar(
                routine = routine,
            )
        },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = Duration.fromSeconds(remainSeconds).toDisplayString())
            }
            ControlButtons(
                onClickNext = onClickNext,
                onClickPrevious = onClickPrevious,
                onClickPlay = onClickPlay,
                isEnabledPreviousButton = isEnabledPreviousButton,
                isEnabledNextButton = isEnabledNextButton,
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
fun RunRoutineTopBar(
    routine: Routine,
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
            Text(
                text = routine.name,
            )
        }
    }
}

@Composable
private fun ControlButtons(
    onClickNext: () -> Unit = {},
    onClickPrevious: () -> Unit = {},
    onClickPlay: () -> Unit = {},
    isEnabledPreviousButton: Boolean,
    isEnabledNextButton: Boolean,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClickPrevious,
            modifier = Modifier.size(64.dp),
            enabled = isEnabledPreviousButton,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    .clickable {
                        onClickPlay()
                    },
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onClickNext,
            modifier = Modifier.size(64.dp),
            enabled = isEnabledNextButton,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun runRoutineContentPreview() {
    runRoutineContent(
        routine =
            Routine(
                id = 0,
                name = "testRoutine",
                tasks =
                    listOf(
                        Task(
                            id = 0,
                            name = "testTask",
                            minutes = 1,
                            seconds = 0,
                            announceRemainingTimeFlag = true,
                        ),
                    ),
            ),
        remainSeconds = 100,
        onClickNext = {},
        onClickPrevious = {},
        onClickPlay = {},
        isEnabledPreviousButton = false,
        isEnabledNextButton = true,
    )
}
