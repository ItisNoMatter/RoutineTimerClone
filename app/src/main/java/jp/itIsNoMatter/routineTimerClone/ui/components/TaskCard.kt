package jp.itIsNoMatter.routineTimerClone.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Task

enum class NodePosition {
    FIRST,
    MIDDLE,
    LAST,
    MOVING,
}

@Composable
fun TaskCard(
    task: Task,
    position: NodePosition,
    onClick: () -> Unit = {},
) {
    Box {
        Card(
            modifier =
                Modifier
                    .height(80.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .clickable {
                        onClick()
                    },
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape,
                            ),
                )
                Text(text = task.name)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = task.duration.toDisplayString())
            }
        }
        Connector(position = position)
    }
}

@Composable
fun Connector(position: NodePosition) {
    val lineColor = MaterialTheme.colorScheme.secondary
    Canvas(
        modifier =
            Modifier
                .height(80.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    clip = false
                },
    ) {
        val x = 44.dp.toPx()
        val upperLineTopY = 0f
        val upperLineBottomY = size.height / 2 - 11.5.dp.toPx()
        val lowerLineTopY = size.height / 2 + 11.5.dp.toPx()
        val lowerLineBottomY = size.height
        when (position) {
            NodePosition.FIRST -> {
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(x, lowerLineTopY),
                    end = androidx.compose.ui.geometry.Offset(x, lowerLineBottomY),
                    strokeWidth = 8.dp.toPx(),
                )
            }
            NodePosition.MIDDLE -> {
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(x, upperLineTopY),
                    end = androidx.compose.ui.geometry.Offset(x, upperLineBottomY),
                    strokeWidth = 8.dp.toPx(),
                )
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(x, lowerLineTopY),
                    end = androidx.compose.ui.geometry.Offset(x, lowerLineBottomY),
                    strokeWidth = 8.dp.toPx(),
                )
            }
            NodePosition.LAST -> {
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(x, upperLineTopY),
                    end = androidx.compose.ui.geometry.Offset(x, upperLineBottomY),
                    strokeWidth = 8.dp.toPx(),
                )
            }
            NodePosition.MOVING -> {}
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 15525616,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait",
)
@Composable
fun TaskCardPreview() {
    Column {
        TaskCard(
            task =
                Task(
                    id = 1,
                    name = "test",
                    duration = Duration(minutes = 1, seconds = 2),
                ),
            position = NodePosition.FIRST,
        )
        TaskCard(
            task =
                Task(
                    id = 1,
                    name = "test",
                    duration = Duration(minutes = 1, seconds = 2),
                ),
            position = NodePosition.MIDDLE,
        )
        TaskCard(
            task =
                Task(
                    id = 1,
                    name = "test",
                    duration = Duration(minutes = 1, seconds = 2),
                ),
            position = NodePosition.LAST,
        )
    }
}
