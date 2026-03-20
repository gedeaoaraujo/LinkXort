package com.emeraldscrolls.linkxort

import com.emeraldscrolls.linkxort.remote.InMemoryApi

fun injectMainRepo(): MainRepository {
  return MainRepository(InMemoryApi())
}

fun injectMainVM(): MainViewModel {
  return MainViewModel()
}