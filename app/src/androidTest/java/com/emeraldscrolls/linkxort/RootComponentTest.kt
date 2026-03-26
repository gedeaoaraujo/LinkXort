package com.emeraldscrolls.linkxort

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.emeraldscrolls.linkxort.components.RootComponent
import org.junit.Rule
import org.junit.Test


class RootComponentTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun whenDialogStateIsTrue_dialogShouldBeDisplayed() {
    val testMessage = "testing"
    val mainState = MainState(showDialog = true, message = testMessage)

    composeTestRule.setContent {
      RootComponent(state = mainState)
    }

    composeTestRule.onNodeWithText("Message").assertIsDisplayed()
    composeTestRule.onNodeWithText(testMessage).assertIsDisplayed()
    composeTestRule.onNodeWithText("Ok").performClick()

    assert(mainState.message == testMessage)
  }

  @Test
  fun whenDialogStateIsFalse_dialogShouldNotBeDisplayed() {
    val testMessage = "testing"
    val mainState = MainState(showDialog = false, message = testMessage)

    composeTestRule.setContent {
      RootComponent(state = mainState)
    }

    composeTestRule.onNodeWithText("Message").assertIsNotDisplayed()
    composeTestRule.onNodeWithText(testMessage).assertIsNotDisplayed()

    assert(mainState.message == testMessage)
  }

  @Test
  fun whenLoadingIsTrue_progressIndicatorShouldBeDisplayed() {
    val mainState = MainState(loading = true)

    composeTestRule.setContent {
      RootComponent(state = mainState)
    }

    composeTestRule.onNodeWithTag("Loading").assertIsDisplayed()
  }

  @Test
  fun whenLoadingIsFalse_progressIndicatorShouldNotBeDisplayed() {
    val mainState = MainState(loading = false)

    composeTestRule.setContent {
      RootComponent(state = mainState)
    }

    composeTestRule.onNodeWithTag("Loading").assertIsNotDisplayed()
  }

}