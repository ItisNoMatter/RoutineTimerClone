package com.example.routinetimerclone.ui.routineList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.routinetimerclone.domain.model.Routine

@Composable
fun RoutineListScreen(
    viewModel: RoutineListViewModel = hiltViewModel(),
    navController: NavHostController,
    onAddRoutineClick: () -> Unit = {},
) {
    val routines by viewModel.routines.collectAsState(emptyList())
    RoutineListContent(
        routines = routines,
        navController = navController,
        onAddRoutineClick = onAddRoutineClick,
    )
}

@Composable
fun RoutineCard(
    routine: Routine,
    onPlayButtonClick: (routineId: Long) -> Unit = {},
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier =
            Modifier
                .padding(16.dp)
                .height(80.dp)
                .fillMaxWidth(),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = routine.name,
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedIconButton(
                onClick = { onPlayButtonClick(routine.id) },
                colors =
                    IconButtonDefaults.outlinedIconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                border = null,
            ) {
                Icon(Icons.Filled.PlayArrow, "Start Routine")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoutineListContent(
    routines: List<Routine>,
    navController: NavHostController,
    onAddRoutineClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "eEffort",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                    )
                },
                modifier =
                    Modifier
                        .height(104.dp),
            )
        },
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRoutineClick) {
                Icon(Icons.Filled.Add, "Add Routine")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            items(routines) { routine ->
                RoutineCard(
                    routine = routine,
                    onPlayButtonClick = { routineId -> navController.navigate("edit/$routineId") },
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun RoutineCardPreview() {
    Column {
        RoutineCard(Routine(1, "routine1", emptyList()))
        RoutineCard(Routine(2, "routine2", emptyList()))
        RoutineCard(Routine(3, "routine3", emptyList()))
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun RoutineListScreenPreview() {
    RoutineListContent(
        navController = NavHostController(LocalContext.current),
        routines =
            listOf(
                Routine(1, "Routine 1", emptyList()),
                Routine(2, "Routine 2", emptyList()),
                Routine(3, "Routine 3", emptyList()),
            ),
    )
}
