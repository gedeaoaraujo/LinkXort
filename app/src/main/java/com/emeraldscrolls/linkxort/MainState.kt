package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.AliasResponse

data class MainState(
  val message: String = "",
  val loading: Boolean = false,
  val showDialog: Boolean = false,
  val links: Set<AliasResponse> = setOf(),
)