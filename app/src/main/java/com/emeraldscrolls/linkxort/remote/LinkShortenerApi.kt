package com.emeraldscrolls.linkxort.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

open class LinkShortenerApi {

  companion object {
    private const val BASE_URL = "http://localhost:8080"
  }

  private val api: LinkService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build().create<LinkService>()
  }

  suspend fun shortLink(link: Link) = api.shortLink(link)
  suspend fun shortLinkByAlias(alias: String) = api.shortLinkByAlias(alias)

}