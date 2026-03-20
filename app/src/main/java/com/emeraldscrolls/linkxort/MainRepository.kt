package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.AliasAndLinksResponse
import com.emeraldscrolls.linkxort.remote.Link
import com.emeraldscrolls.linkxort.remote.LinkShortenerApi
import com.emeraldscrolls.linkxort.remote.Result
import com.google.gson.JsonSyntaxException
import okio.IOException
import retrofit2.HttpException
import java.net.HttpURLConnection

class MainRepository(
  private val api: LinkShortenerApi
) {

  suspend fun shortLink(link: String): Result<AliasAndLinksResponse> {
    return try {
      val result = api.shortLink(Link(link))
      return if (result.isSuccessful) {
        Result.Success(result.body()!!)
      } else {
        val msg = "Request error Code: ${result.code()}"
        Result.Error(msg, result.code())
      }
    } catch (_: IOException){
      Result.Error("An error occurred on server connection.")
    } catch (_: JsonSyntaxException){
      Result.Error("An error occurred on server response.")
    } catch (_: HttpException) {
      Result.Error("An error occurred on server response.")
    } catch (_: Throwable) {
      Result.Error("Internal request error.")
    }
  }

  suspend fun shortLinkyByAlias(alias: String): Result<Link> {
    return try {
      val result = api.shortLinkByAlias(alias)
      when (result.code()) {
        HttpURLConnection.HTTP_NOT_FOUND -> {
          Result.Error("Alias not found.")
        }
        else -> {
          if (result.isSuccessful) {
            Result.Success(result.body()!!)
          } else {
            val msg = "Request error Code: ${result.code()}."
            Result.Error(msg, result.code())
          }
        }
      }
    } catch (_: IOException){
      Result.Error("An error occurred on server connection.")
    } catch (_: JsonSyntaxException){
      Result.Error("An error occurred on server response.")
    } catch (_: HttpException) {
      Result.Error("An error occurred on server response.")
    } catch (_: Throwable) {
      Result.Error("Internal request error.")
    }
  }

}