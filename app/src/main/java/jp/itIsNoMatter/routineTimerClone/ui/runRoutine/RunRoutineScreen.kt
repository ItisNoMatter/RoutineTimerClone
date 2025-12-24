package jp.itIsNoMatter.routineTimerClone.ui.runRoutine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine

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
fun runRoutineContent(routine: Routine) {
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
            Text(text = routine.name)
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = routine.tasks[0].name)
            }
            IconButton(
                onClick = {},
            ) {
                Icon(Icons.Filled.PlayArrow, "Start Routine")
            }
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
