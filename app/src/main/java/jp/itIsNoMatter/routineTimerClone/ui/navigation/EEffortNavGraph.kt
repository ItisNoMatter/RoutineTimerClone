package jp.itIsNoMatter.routineTimerClone.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import jp.itIsNoMatter.routineTimerClone.ui.routineCreate.RoutineCreateScreen
import jp.itIsNoMatter.routineTimerClone.ui.routineEdit.RoutineEditScreen
import jp.itIsNoMatter.routineTimerClone.ui.routineList.RoutineListScreen
import jp.itIsNoMatter.routineTimerClone.ui.task.create.TaskCreateScreen

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
        composable<Route.TaskCreate> {
                backStackEntry ->
            TaskCreateScreen(
                navHostController = navController,
                parentRoutineId = backStackEntry.toRoute<Route.TaskCreate>().routineId,
            )
        }
    }
}
