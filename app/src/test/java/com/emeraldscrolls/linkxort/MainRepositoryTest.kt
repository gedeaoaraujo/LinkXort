package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.AliasResponse
import com.emeraldscrolls.linkxort.remote.Link
import com.emeraldscrolls.linkxort.remote.LinkShortenerApi
import com.emeraldscrolls.linkxort.remote.UrlResponse
import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import com.emeraldscrolls.linkxort.remote.Result

class MainRepositoryTest {

  private val api = mockk<LinkShortenerApi>()
  private val repository = MainRepository(api)

  @Test
  fun `When post a valid url respond a valid alias`(): Unit = runBlocking {
    val alias = "123"
    val urlTest = "https://test.nu"

    coEvery {
      api.shortLink(Link(urlTest))
    } returns Response.success(
      AliasResponse(
        alias = "123", urls = UrlResponse("", "")
      )
    )

    val result = repository.shortLink(urlTest)

    assert(result is Result.Success)
    assertEquals(alias, (result as Result.Success).data.alias)
  }

  @Test
  fun `When request a valid GET should return a valid url`() = runBlocking {
    val aliasTest = "123"
    val urlTest = "https://test.nu"

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } returns Response.success(Link(urlTest))

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Success)
    assertEquals(urlTest, (result as Result.Success).data.url)
  }

  @Test
  fun `When server connection error on shortLink should catch exception`(): Unit = runBlocking {
    val urlTest = "https://test.nu"
    val errorMessage = "An error occurred on server connection."

    coEvery {
      api.shortLink(Link(urlTest))
    } throws okio.IOException()

    val result = repository.shortLink(urlTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When api returns invalid object on shortLink should catch exception`(): Unit = runBlocking {
    val urlTest = "https://test.nu"
    val errorMessage = "An error occurred on server response."

    coEvery {
      api.shortLink(Link(urlTest))
    } throws JsonSyntaxException(errorMessage)

    val result = repository.shortLink(urlTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When server response error on shortLink should catch exception`(): Unit = runBlocking {
    val urlTest = "https://test.nu"
    val errorMessage = "An error occurred on server response."

    coEvery {
      api.shortLink(Link(urlTest))
    } throws HttpException(Response.error<Link>(
      500, "".toResponseBody(null)
    ))

    val result = repository.shortLink(urlTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When internal error on shortLink should catch exception`(): Unit = runBlocking {
    val urlTest = "https://test.nu"
    val errorMessage = "Internal request error."

    coEvery {
      api.shortLink(Link(urlTest))
    } throws Exception()

    val result = repository.shortLink(urlTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When server returns 404 on shortLinkyByAlias should catch Error`() = runBlocking {
    val aliasTest = "123"
    val errorMessage = "Alias not found."

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } returns Response.error(
      404, "".toResponseBody(null)
    )

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When api returns invalid object on shortLinkyByAlias should catch exception`(): Unit = runBlocking {
    val aliasTest = "123"
    val errorMessage = "An error occurred on server response."

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } throws JsonSyntaxException("Malformed JSON")

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When server connection error on shortLinkyByAlias should catch exception`(): Unit = runBlocking {
    val aliasTest = "123"
    val errorMessage = "An error occurred on server connection."

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } throws IOException()

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When server response error on shortLinkyByAlias should catch exception`(): Unit = runBlocking {
    val aliasTest = "123"
    val errorMessage = "An error occurred on server response."

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } throws HttpException(Response.error<Link>(
      500, "".toResponseBody(null)
    ))

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

  @Test
  fun `When internal error on shortLinkByAlias should catch exception`(): Unit = runBlocking {
    val aliasTest = "123"
    val errorMessage = "Internal request error."

    coEvery {
      api.shortLinkByAlias(aliasTest)
    } throws Exception()

    val result = repository.shortLinkyByAlias(aliasTest)
    assert(result is Result.Error)
    assertEquals(errorMessage, (result as Result.Error).message)
  }

}