package com.emeraldscrolls.linkxort

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emeraldscrolls.linkxort.ui.theme.LinkXortTheme

@Composable
fun RootApp(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(text = "Hello LinkXort.")
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  LinkXortTheme {
    RootApp()
  }
}