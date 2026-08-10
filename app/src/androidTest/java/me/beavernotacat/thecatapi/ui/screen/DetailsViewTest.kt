package me.beavernotacat.thecatapi.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertTrue
import me.beavernotacat.thecatapi.data.abys
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.LoadingDetailsStates
import me.beavernotacat.thecatapi.ui.components.DetailsComponent
import me.beavernotacat.thecatapi.ui.views.DetailsView
import org.junit.Rule
import org.junit.Test

class DetailsComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `cat details displays correctly`() {
        val catInfo = abys
        val imageId = abys.imageId ?: error("image id never should be null here")

        val image = CatImage.LoadedCatImage("https://example.com/cat.jpg")

        val state = LoadingDetailsStates.Ok(
            cat = catInfo
        )

        composeTestRule.setContent {
            DetailsView(
                details = state,
                images = mapOf(Pair(imageId, image)),
                favorite = false,
                patState = CatPatState.NotFound,
                addToFavorites = { },
                onRetry = { },
                onUpdatePatState = { }
            )
        }

        composeTestRule.onNodeWithText("Abyssinian").assertIsDisplayed()
        composeTestRule.onNodeWithText(catInfo.description).assertIsDisplayed()

        composeTestRule.onNodeWithTag("details value Weight").assertTextEquals("3 - 5 kg")
        composeTestRule.onNodeWithTag("details label Weight").assertTextEquals("Weight")

        composeTestRule.onNodeWithTag("details value Indoor").assertTextEquals("⛔️")
        composeTestRule.onNodeWithTag("details value Adaptability").assertTextEquals("⭐⭐⭐⭐⭐")

    }

    @Test
    fun detailsComponent_showsHateButtonWhenFavorite() {
        val favoriteClicked = mutableStateOf(false)
        composeTestRule.setContent {
            DetailsComponent(
                catInfo = abys,
                image = null,
                favorite = favoriteClicked.value,
                patState = CatPatState.NotFound,
                addToFavorites = { favoriteClicked.value = !favoriteClicked.value },
                onUpdatePatState = { }
            )
        }

        composeTestRule.onNodeWithText("I love this cat!").assertIsDisplayed()
        composeTestRule.onNodeWithText("I love this cat!").performClick()

        assertTrue(favoriteClicked.value)

        composeTestRule.onNodeWithText("I love this cat!").assertDoesNotExist()
        composeTestRule.onNodeWithText("Now i hate this cat.").assertIsDisplayed()
    }
}