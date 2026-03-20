package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.AliasAndLinksResponse

data class MainState(
  val message: String = "",
  val loading: Boolean = false,
  val links: Set<AliasAndLinksResponse> = setOf(),
)