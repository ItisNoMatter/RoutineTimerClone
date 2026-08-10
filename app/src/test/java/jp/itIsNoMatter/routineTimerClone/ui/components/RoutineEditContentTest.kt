package jp.itIsNoMatter.routineTimerClone.ui.components

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.mockk.mockk
import io.mockk.verify
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class RoutineEditContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val routine =
        Routine(
            id = "routine-1",
            name = "test routine",
            tasks =
                listOf(
                    Task(id = "task-1", name = "task A", duration = Duration(1, 2), announceRemainingTimeFlag = true),
                    Task(id = "task-2", name = "task B", duration = Duration(3, 15), announceRemainingTimeFlag = true),
                ),
        )

    @Test
    fun `初期表示 - ルーチン名・各タスク名時間・合計時間が表示されること`() {
        composeTestRule.setContent {
            RoutineEditContent(routine = routine)
        }

        composeTestRule.onNodeWithText(routine.name).assertExists()
        routine.tasks.forEach { task ->
            composeTestRule.onNodeWithText(task.name).assertExists()
            composeTestRule.onNodeWithText(task.duration.toDisplayString()).assertExists()
        }
        composeTestRule.onNodeWithText("計 " + routine.getTotalDuration().toDisplayString()).assertExists()
    }

    @Test
    fun `FABタップでonClickAddButtonが呼ばれること`() {
        val onClickAddButton = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, onClickAddButton = onClickAddButton)
        }

        composeTestRule.onNodeWithContentDescription("Add Task").performClick()

        verify { onClickAddButton() }
    }

    @Test
    fun `戻るボタンタップでonClickBackButtonが呼ばれること`() {
        val onClickBackButton = mockk<() -> Unit>(relaxed = true)
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, onClickBackButton = onClickBackButton)
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        verify { onClickBackButton() }
    }

    @Test
    fun `タスクカードタップでonClickTaskCardがそのタスクのidで呼ばれること`() {
        val onClickTaskCard = mockk<(String) -> Unit>(relaxed = true)
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, onClickTaskCard = onClickTaskCard)
        }

        composeTestRule.onNodeWithText(routine.tasks[1].name).performClick()

        verify { onClickTaskCard(routine.tasks[1].id) }
    }

    @Test
    fun `タイトル入力でonRoutineTitleChangeが呼ばれること`() {
        val onRoutineTitleChange = mockk<(String) -> Unit>(relaxed = true)
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, onRoutineTitleChange = onRoutineTitleChange)
        }

        composeTestRule.onNodeWithTag(ROUTINE_TITLE_TEXT_FIELD_TEST_TAG).performTextInput("!")

        verify { onRoutineTitleChange(routine.name + "!") }
    }

    @Test
    fun `isBackButtonEnabledがfalseのとき戻るボタンが無効化されること`() {
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, isBackButtonEnabled = false)
        }

        composeTestRule.onNodeWithContentDescription("Back").assertIsNotEnabled()
    }

    @Test
    fun `isBackButtonEnabled未指定時はisSavingの否定がデフォルトになること`() {
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, isSaving = true)
        }

        composeTestRule.onNodeWithContentDescription("Back").assertIsNotEnabled()
    }

    @Test
    fun `isBackButtonEnabled未指定かつisSavingがfalseのとき戻るボタンが有効であること`() {
        composeTestRule.setContent {
            RoutineEditContent(routine = routine, isSaving = false)
        }

        composeTestRule.onNodeWithContentDescription("Back").assertIsEnabled()
    }
}
