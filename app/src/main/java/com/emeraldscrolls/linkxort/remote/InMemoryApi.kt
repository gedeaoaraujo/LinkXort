package com.emeraldscrolls.linkxort.remote

import retrofit2.Response

class InMemoryApi: ILinkService {

  private var id = 99
  private val links = mutableListOf<AliasResponse>()

  override suspend fun shortLink(link: Link): Response<AliasResponse> {
    val newLink = UrlResponse(
      originalUrl = link.url,
      apiUrl = "https://testing.com/$id"
    )
    val element = AliasResponse(alias = "${++id}", urls = newLink)
    links.add(element)
    return Response.success(element)
  }

  override suspend fun shortLinkByAlias(alias: String): Response<Link> {
    val res = links.first { it.alias == alias }
    return Response.success(Link(res.urls.originalUrl))
  }
}