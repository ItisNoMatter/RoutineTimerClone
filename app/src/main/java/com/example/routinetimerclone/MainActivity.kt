package com.example.routinetimerclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.routinetimerclone.ui.routineEdit.RoutineEditScreen
import com.example.routinetimerclone.ui.routineList.RoutineListScreen
import com.example.routinetimerclone.ui.theme.RoutineTimerCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutineTimerCloneTheme {
                EEffortNavGraph()
            }
        }
    }
}

sealed interface Route {
    @Serializable
    data object RoutineList : Route

    @Serializable
    data class RoutineEdit(
        val routineId: Long,
    ) : Route
}

@Composable
fun EEffortNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.RoutineList,
    ) {
        composable<Route.RoutineList> {
            RoutineListScreen(
                navController = navController,
            )
        }
        composable<Route.RoutineEdit> {
                backStackEntry ->
            RoutineEditScreen(
                navHostController = navController,
                routineId = backStackEntry.toRoute<Route.RoutineEdit>().routineId,
            )
        }
    }
}
