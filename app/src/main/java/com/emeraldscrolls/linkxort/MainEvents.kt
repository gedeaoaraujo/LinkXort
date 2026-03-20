package com.emeraldscrolls.linkxort

class MainEvents(
  val onClearMessage: () -> Unit = {},
  val onShortLink: (String) -> Unit = {},
  val onShortLinkByAlias: (String) -> Unit = {},
)