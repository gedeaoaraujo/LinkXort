package com.emeraldscrolls.linkxort.remote

import retrofit2.Response

interface ILinkService {
  suspend fun shortLink(link: Link): Response<AliasAndLinksResponse>
  suspend fun shortLinkByAlias(alias: String): Response<Link>
}