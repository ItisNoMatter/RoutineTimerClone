package com.example.routineTimerClone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.routineTimerClone.ui.navigation.EEffortNavGraph
import com.example.routineTimerClone.ui.theme.RoutineTimerCloneTheme
import dagger.hilt.android.AndroidEntryPoint

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
