package com.emeraldscrolls.linkxort

sealed class MainIntent {
  object ClearMessage: MainIntent()
  data class ShortLink(val link: String): MainIntent()
  data class ShortLinkByAlias(val alias: String): MainIntent()
}