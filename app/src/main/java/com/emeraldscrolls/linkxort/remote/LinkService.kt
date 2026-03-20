package com.emeraldscrolls.linkxort.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LinkService {

  @POST("api/alias")
  suspend fun shortLink(@Body link: Link): Response<AliasResponse>

  @GET("api/alias/{id}")
  suspend fun shortLinkByAlias(@Path("id") alias: String): Response<Link>

}