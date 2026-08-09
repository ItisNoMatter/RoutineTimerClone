package jp.itIsNoMatter.routineTimerClone.ui.routinelist

import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class RoutineListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val routines = (1..20).map { Routine(it.toString(), "Routine $it", emptyList()) }

    @Test
    fun `末尾までスクロールしても再生ボタンがFABと重ならない`() {
        composeTestRule.setContent {
            RoutineListContent(routines = routines)
        }

        composeTestRule.onNodeWithTag(ROUTINE_LIST_TEST_TAG).performTouchInput {
            repeat(8) { swipeUp() }
        }
        composeTestRule.waitForIdle()

        val fabBounds = composeTestRule.onNodeWithContentDescription("Add Routine").getBoundsInRoot()
        val lastPlayButtonBounds =
            composeTestRule
                .onAllNodesWithContentDescription("Start Routine")
                .onLast()
                .getBoundsInRoot()

        assertTrue(lastPlayButtonBounds.bottom <= fabBounds.top)
    }
}
