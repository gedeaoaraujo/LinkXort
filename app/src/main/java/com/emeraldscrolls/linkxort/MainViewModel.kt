package com.emeraldscrolls.linkxort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emeraldscrolls.linkxort.remote.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repository: MainRepository,
  private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state: StateFlow<MainState> = _state

  val events = MainEvents(
    onShortLink = ::shortLink,
    onClearMessage = ::clearMessage,
    onShortLinkByAlias = ::shortLinkByAlias
  )

  fun clearMessage() {
    _state.update { it.copy(
      message = "", showDialog = false
    )}
  }

  fun shortLink(link: String) = viewModelScope.launch(ioDispatcher) {
    _state.update { it.copy(loading = true) }
    when (val response = repository.shortLink(link)) {
      is Result.Success -> {
        _state.update {
          it.copy(
            loading = false,
            links = it.links + response.data
          )
        }
      }
      is Result.Error -> {
        _state.update { it.copy(
          loading = false,
          showDialog = true,
          message = response.message
        )}
      }
    }
  }

  fun shortLinkByAlias(alias: String) = viewModelScope.launch(ioDispatcher) {
    _state.update { it.copy(loading = true) }
    when (val response = repository.shortLinkyByAlias(alias)){
      is Result.Success -> {
        val msg = "The URL of alias $alias is: ${response.data.url}."
        _state.update { it.copy(
          message = msg,
          loading = false,
          showDialog = true
        )}
      }
      is Result.Error -> {
        _state.update { it.copy(
          loading = false,
          showDialog = true,
          message = response.message
        )}
      }
    }
  }

}