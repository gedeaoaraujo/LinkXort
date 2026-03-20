package com.emeraldscrolls.linkxort

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.emeraldscrolls.linkxort.remote.AliasResponse
import com.emeraldscrolls.linkxort.remote.UrlResponse
import org.junit.Rule
import org.junit.Test

class HomePageTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun should_ShowInitialElements() {
    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("URL to cut").assertIsDisplayed()
    composeTestRule.onNodeWithText("Recently shortened URLs").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Send").assertIsDisplayed()
  }

  @Test
  fun when_TypingUrl_and_ClickingSend_should_CallShortLink() {
    var calledUrl = ""
    val testUrl = "http://test.com"
    val events = MainEvents(onShortLink = { calledUrl = it })

    composeTestRule.setContent {
      HomePage(events = events)
    }

    composeTestRule.onNodeWithText("").performTextInput(testUrl)
    composeTestRule.onNodeWithContentDescription("Send").performClick()

    assert(calledUrl == testUrl)
  }

  @Test
  fun when_TypingNumbers_and_ClickingSend_should_CallShortLinkByAlias() {
    var calledId = ""
    val testAlias = "12345"
    val events = MainEvents(onShortLinkByAlias = { calledId = it })

    composeTestRule.setContent {
      HomePage(events = events)
    }

    composeTestRule.onNodeWithText("").performTextInput(testAlias)
    composeTestRule.onNodeWithContentDescription("Send").performClick()

    assert(calledId == testAlias)
  }

  @Test
  fun should_NotAllowMoreThan256Characters() {
    val longText = "a".repeat(300)
    val expectedText = "a".repeat(256)

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(longText)
    composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
  }

  @Test
  fun should_ShowErrorTextOnInvalidInput_Case1() {
    val urlTest = "zzzzzz"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsDisplayed()
  }

  @Test
  fun should_ShowErrorTextOnInvalidInput_Case2() {
    val urlTest = "123456789x"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsDisplayed()
  }

  @Test
  fun should_ShowErrorTextOnInvalidInput_Case3() {
    val urlTest = "http"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsDisplayed()
  }

  @Test
  fun should_ShowErrorTextOnInvalidInput_Case4() {
    val urlTest = "https"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsDisplayed()
  }

  @Test
  fun should_ShowErrorTextOnInvalidInput_Case5() {
    val urlTest = "http://."
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsDisplayed()
  }

  @Test
  fun should_HideErrorTextOnValidHttpUrl() {
    val urlTest = "http://sou.nu"
    val invalidURl = "zzzzzzzzzzzz"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(invalidURl)
    composeTestRule.onNodeWithContentDescription("Send").performClick()

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsNotDisplayed()
  }

  @Test
  fun should_HideErrorTextOnValidHttpsUrl() {
    val urlTest = "https://sou.nu"
    val invalidURl = "zzzzzzzzzzzz"
    val messageTest = "Text must be an alias, http or https urls."

    composeTestRule.setContent {
      HomePage(state = MainState())
    }

    composeTestRule.onNodeWithText("").performTextInput(invalidURl)
    composeTestRule.onNodeWithContentDescription("Send").performClick()

    composeTestRule.onNodeWithText("").performTextInput(urlTest)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText(messageTest).assertIsNotDisplayed()
  }

  @Test
  fun insertUrl_clicksSend_andVerifiesIfItemAppearsInList() {
    val testUrl = "https://test1.com"
    val fakeState = mutableStateOf(MainState())
    val fakeEvents = MainEvents(onShortLink = { url ->
      val newItem = AliasResponse(
        "123", UrlResponse(testUrl, "")
      )
      fakeState.value = MainState(links = setOf(newItem))
    })

    composeTestRule.setContent {
      HomePage(
        state = fakeState.value,
        events = fakeEvents
      )
    }

    composeTestRule.onNodeWithText("").performTextInput(testUrl)
    composeTestRule.onNodeWithContentDescription("Send").performClick()
    composeTestRule.onNodeWithText("Original: $testUrl" ).assertIsDisplayed()
  }
}