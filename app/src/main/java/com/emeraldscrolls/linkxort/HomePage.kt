package com.emeraldscrolls.linkxort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun HomePage(
  modifier: Modifier = Modifier,
  state: MainState = MainState(),
  events: MainEvents = MainEvents()
) {
  var urlText by remember { mutableStateOf("") }
  var showErrorText by remember { mutableStateOf(false) }
  var list by remember { mutableStateOf<List<String>>(mutableListOf()) }

  val shortLink: (item: String) -> Unit = { item ->
    list += item
  }
  val shortLinkByAlias: (String) -> Unit = {}

  fun onSendClicked() {
    val url = urlText
    if (url.isBlank()) return
    urlText = ""

    if (url.isDigitsOnly()){
      shortLinkByAlias(url)
      showErrorText = false
      return
    }

    val regexUrl = "https?://.+\\..+".toRegex()
    if (regexUrl.matches(url)) {
      shortLink(url)
      showErrorText = false
      return
    }

    showErrorText = true
  }

  fun onValueChange(value: String) {
    if (value.count() < 256) {
      urlText = value
    } else {
      urlText = value.substring(0, 256)
    }
  }

  Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Column(modifier.padding(18.dp)) {
      Column {
        Text("URL to cut", fontWeight = FontWeight.Bold)
        Spacer(modifier.height(8.dp))
        Row {
          TextField(
            singleLine = true,
            value = urlText,
            onValueChange = { onValueChange(it) },
            modifier = modifier.weight(1f),
            shape = CircleShape.copy(CornerSize(10.dp)),
            colors = TextFieldDefaults.colors(
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent
            )
          )
          IconButton(onClick = { onSendClicked() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              modifier = modifier
                .size(30.dp)
                .padding(top = 2.dp),
            )
          }
        }
      }
      if (showErrorText) Text(
        text = "Text must be an alias, http or https urls.",
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier.padding(top = 4.dp),
        color = Color.Red
      )
      Spacer(modifier.height(16.dp))
      Text("Recently shortened URLs", fontWeight = FontWeight.Bold)
      Spacer(modifier.height(16.dp))
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(list) { item ->
          LinkItem(item)
        }
      }
    }
  }
}