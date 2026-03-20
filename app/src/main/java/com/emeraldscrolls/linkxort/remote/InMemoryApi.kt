package com.emeraldscrolls.linkxort.remote

import retrofit2.Response

class InMemoryApi: ILinkService {

  private var id = 99
  private val links = mutableListOf<AliasAndLinksResponse>()

  override suspend fun shortLink(link: Link): Response<AliasAndLinksResponse> {
    val newLink = LinksResponse(link.url, "https://testing.com/$id")
    val element = AliasAndLinksResponse(alias = "${++id}", _links = newLink)
    links.add(element)
    return Response.success(element)
  }

  override suspend fun shortLinkByAlias(alias: String): Response<Link> {
    val res = links.first { it.alias == alias }
    return Response.success(Link(res._links.self))
  }
}