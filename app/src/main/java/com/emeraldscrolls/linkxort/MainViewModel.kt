package com.emeraldscrolls.linkxort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emeraldscrolls.linkxort.remote.LinkShortenerApi
import com.emeraldscrolls.linkxort.remote.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
  private val repository: MainRepository = MainRepository(LinkShortenerApi()),
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state: StateFlow<MainState> = _state

  val events = MainEvents(
    onShortLink = ::shortLink,
    onClearMessage = ::clearMessage,
    onShortLinkByAlias = ::shortLinkByAlias
  )

  fun clearMessage() {
    _state.update { it.copy(message = "") }
  }

  fun shortLink(link: String) = viewModelScope.launch(ioDispatcher) {
    _state.update { it.copy(loading = true) }
    when (val response = repository.shortLink(link)) {
      is Result.Success -> {
        _state.update {
          val elements = it.links.toTypedArray()
          it.copy(links = setOf(*elements, response.data))
        }
      }
      is Result.Error -> {
        _state.update { it.copy(message = response.message) }
      }
    }
    _state.update { it.copy(loading = false) }
  }

  fun shortLinkByAlias(alias: String) = viewModelScope.launch(ioDispatcher) {
    _state.update { it.copy(loading = true) }
    when (val response = repository.shortLinkyByAlias(alias)){
      is Result.Success -> {
        val msg = "The URL of alias $alias is: ${response.data.url}."
        _state.update { it.copy(message = msg) }
      }
      is Result.Error -> {
        _state.update { it.copy(message = response.message) }
      }
    }
    _state.update { it.copy(loading = false) }
  }

}