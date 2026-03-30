package com.emeraldscrolls.linkxort.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.emeraldscrolls.linkxort.MainIntent
import com.emeraldscrolls.linkxort.MainState

@Composable
fun RootComponent(
  modifier: Modifier = Modifier,
  state: MainState = MainState(),
  onAction: (MainIntent) -> Unit = {}
) {
  HomePage(modifier, state, onAction)

  if (state.loading) {
    CircularProgressIndicator(
      modifier = Modifier.width(40.dp).testTag("Loading"),
      color = MaterialTheme.colorScheme.secondary,
      trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }

  if (state.showDialog) {
    AlertDialog(
      title = { Text("Message") },
      onDismissRequest = {
        onAction(MainIntent.ClearMessage)
      },
      text = { Text(state.message) },
      confirmButton = {
        TextButton(onClick = {
          onAction(MainIntent.ClearMessage)
        }) { Text("Ok") }
      }
    )
  }
}