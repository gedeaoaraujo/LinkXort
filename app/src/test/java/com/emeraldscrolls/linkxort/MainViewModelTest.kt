package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.AliasResponse
import com.emeraldscrolls.linkxort.remote.Link
import com.emeraldscrolls.linkxort.remote.Result
import com.emeraldscrolls.linkxort.remote.UrlResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

  private val repository = mockk<MainRepository>()
  private val viewModel = MainViewModel(
    repository, UnconfinedTestDispatcher()
  )

  @Test
  fun `shortLink should update links when repository returns success`() = runTest {
    val linkInput = "https://test.com"
    val mockResponse = AliasResponse(
      alias = "123", urls = UrlResponse(linkInput, "")
    )

    coEvery {
      repository.shortLink(linkInput)
    } returns Result.Success(mockResponse)

    viewModel.shortLink(linkInput)

    val state = viewModel.state.value
    assertTrue(state.links.contains(mockResponse))
    assertEquals(1, state.links.size)
  }

  @Test
  fun `shortLink should update error message when repository fails`() = runTest {
    val linkInput = "https://test.com"
    val errorMessage = "Connection error"

    coEvery {
      repository.shortLink(linkInput)
    } returns Result.Error(message = errorMessage)

    viewModel.shortLink(linkInput)

    val state = viewModel.state.value
    assertEquals(errorMessage, state.message)
  }

  @Test
  fun `shortLinkByAlias should show message with URL on success`() = runTest {
    val aliasTest = "123"
    val urlTest = "https://test.com"

    coEvery {
      repository.shortLinkyByAlias(aliasTest)
    } returns Result.Success(Link(urlTest))

    viewModel.shortLinkByAlias(aliasTest)

    val state = viewModel.state.value
    val expectedMsg = "The URL of alias $aliasTest is: https://test.com."
    assertEquals(expectedMsg, state.message)
  }

  @Test
  fun `clearMessage should set message as blank`() = runTest {
    val urlTest = "https://test.com"

    coEvery {
      repository.shortLink(urlTest)
    } returns Result.Error(message = "Error")

    viewModel.shortLink(urlTest)
    assertNotEquals(urlTest, viewModel.state.value.message)

    viewModel.clearMessage()
    assertEquals("", viewModel.state.value.message)
  }
}