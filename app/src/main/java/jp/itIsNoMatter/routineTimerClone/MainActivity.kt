package jp.itIsNoMatter.routineTimerClone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import jp.itIsNoMatter.routineTimerClone.ui.navigation.EEffortNavGraph
import jp.itIsNoMatter.routineTimerClone.ui.theme.RoutineTimerCloneTheme

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
