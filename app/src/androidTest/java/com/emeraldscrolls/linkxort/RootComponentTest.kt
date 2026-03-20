package com.emeraldscrolls.linkxort

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test


class RootComponentTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun whenStateHasMessage_dialogShouldShowAndThenClose() {
    val testAlias = "123456789"
    val testUrl = "http://test.com"
    val message  = "The URL of alias $testAlias is: ${testUrl}."
    var mainState = MainState(message = message)
    val event = { mainState = mainState.copy(message = "") }

    composeTestRule.setContent {
      RootComponent(
        state = mainState,
        events = MainEvents(onClearMessage = event)
      )
    }

    composeTestRule.onNodeWithText("Message").assertIsDisplayed()
    composeTestRule.onNodeWithText(message).assertIsDisplayed()
    composeTestRule.onNodeWithText("Ok").performClick()

    assert(mainState.message == "")
  }

  @Test
  fun whenStateMessageIsEmpty_dialogShouldNotBeVisible() {
    val emptyState = MainState(message = "")

    composeTestRule.setContent {
      RootComponent(state = emptyState)
    }

    composeTestRule.onNodeWithText("Message").assertDoesNotExist()
  }

}