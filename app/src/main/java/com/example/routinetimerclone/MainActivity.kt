package com.example.routinetimerclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Serializable
object RoutineList

@Serializable
data class RoutineEdit(
    val routineId: Long? = null
)

@Composable
fun EEffortNavGraph(
    startDestination: String = "routineList",
) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<RoutineList> {
            RoutineListScreen()
        }
        composable<RoutineEdit> {
            RoutineEditScreen()
        }

    }
}
