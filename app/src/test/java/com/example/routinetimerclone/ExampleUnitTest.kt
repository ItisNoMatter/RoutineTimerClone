package com.example.routinetimerclone

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun GreetingExample(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(text = "Hello $name", modifier = modifier)
}

@RunWith(RobolectricTestRunner::class)
class ComposeUiTestExample {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGreetingExample() {
        composeTestRule.setContent {
            GreetingExample("Android")
        }

        composeTestRule
            .onNodeWithText("Hello Android")
            .assertExists()
    }
}
