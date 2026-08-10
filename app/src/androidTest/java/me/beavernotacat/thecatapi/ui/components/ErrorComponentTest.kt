package me.beavernotacat.thecatapi.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class ErrorComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `errorComponent displays message and onRetry button`() {
        val errorMessage = "Something went wrong"
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithTag("error message").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }


    @Test
    fun `errorComponent calls onRetry when button is clicked`() {
        var retryClicked = false
        composeTestRule.setContent {
            ErrorComponent(
                message = "Error",
                onRetry = { retryClicked = !retryClicked }
            )
        }

        composeTestRule.onNodeWithTag("error retry").performClick()
        assert(retryClicked)
    }
}
