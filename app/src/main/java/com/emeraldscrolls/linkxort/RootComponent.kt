package com.emeraldscrolls.linkxort

import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RootComponent(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = MainViewModel()
) {
  val state by viewModel.state.collectAsState()
  var showDialog by remember { mutableStateOf(false) }

  LaunchedEffect(state.message) {
    if (state.message.isNotBlank()) {
      showDialog = true
    }
  }

  HomePage(modifier, state, viewModel.events)

  if (state.loading) {
    CircularProgressIndicator(
      modifier = Modifier.width(40.dp),
      color = MaterialTheme.colorScheme.secondary,
      trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }

  if (showDialog) {
    AlertDialog(
      title = { Text("Message") },
      onDismissRequest = {
        viewModel.events.onClearMessage()
        showDialog = false
      },
      text = { Text(state.message) },
      confirmButton = {
        TextButton(onClick = {
          viewModel.events.onClearMessage()
          showDialog = false
        }) { Text("Ok") }
      }
    )
  }
}