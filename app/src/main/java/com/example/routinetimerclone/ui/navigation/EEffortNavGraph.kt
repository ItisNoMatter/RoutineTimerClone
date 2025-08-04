package com.example.routinetimerclone.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.routinetimerclone.ui.routine_create.RoutineCreateScreen
import com.example.routinetimerclone.ui.routine_edit.RoutineEditScreen
import com.example.routinetimerclone.ui.routine_list.RoutineListScreen

@Composable
fun EEffortNavGraph() {
    val navController = rememberNavController()
    EEffortNavHost(navController)
}

@Composable
fun EEffortNavHost(navController: NavHostController) {
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
        composable<Route.RoutineCreate> {
                backStackEntry ->
            RoutineCreateScreen(
                navHostController = navController,
            )
        }
    }
}
