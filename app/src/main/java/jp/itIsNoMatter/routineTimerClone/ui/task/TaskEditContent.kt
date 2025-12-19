package jp.itIsNoMatter.routineTimerClone.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.ui.task.create.TaskCreateTopBar

@Composable
fun TaskEditContent(
    taskTitle: String,
    taskDuration: Duration,
    announceFlag: Boolean,
    onTaskNameChange: (String) -> Unit,
    onTaskMinuteChange: (Int) -> Unit,
    onTaskSecondChange: (Int) -> Unit,
    onToggleAnnounceRemainingTimeFlag: (Boolean) -> Unit,
    onClickBackButton: () -> Unit,
) {
    Scaffold(
        topBar = {
            TaskCreateTopBar(
                onClickBackButton = onClickBackButton,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            TaskNameInput(
                taskTitle = taskTitle,
                onTaskNameChange = onTaskNameChange,
            )
            TaskDurationInput(
                duration = taskDuration,
                onTaskMinutesChange = onTaskMinuteChange,
                onTaskSecondChange = onTaskSecondChange,
            )
            AnnounceToggle(
                checked = announceFlag,
                onToggleAnnounceRemainingTimeFlag = onToggleAnnounceRemainingTimeFlag,
            )
        }
    }
}

@Composable
private fun TaskDurationInput(
    duration: Duration,
    onTaskMinutesChange: (Int) -> Unit,
    onTaskSecondChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            NumberInput(
                value = duration.minutes,
                onValueChange = onTaskMinutesChange,
                suffix = "分",
            )
            NumberInput(
                value = (duration.seconds),
                onValueChange = onTaskSecondChange,
                suffix = "秒",
            )
        }
    }
}

@Composable
private fun NumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    suffix: String,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = if (value == 0) "" else value.toString(),
            onValueChange = { stringValue ->
                if (stringValue.all { it.isDigit() }) {
                    val newValue = stringValue.toIntOrNull() ?: 0
                    onValueChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = modifier.width(64.dp),
        )
        Text(text = suffix)
    }
}

@Composable
private fun TaskNameInput(
    taskTitle: String,
    onTaskNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Text(text = "作業の名前")
        TextField(
            value = taskTitle,
            onValueChange = onTaskNameChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun AnnounceToggle(
    checked: Boolean = false,
    onToggleAnnounceRemainingTimeFlag: (Boolean) -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(40.dp)
                .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "残り時間をアナウンスする",
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Switch(
            checked = checked,
            onCheckedChange = onToggleAnnounceRemainingTimeFlag,
        )
    }
}
