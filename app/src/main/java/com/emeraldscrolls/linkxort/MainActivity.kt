package com.emeraldscrolls.linkxort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldscrolls.linkxort.ui.theme.LinkXortTheme

class MainActivity : ComponentActivity() {
  private val viewModel by lazy { MainViewModel() }

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      LinkXortTheme {
        Scaffold(topBar = {
          CenterAlignedTopAppBar(
            title = {
              Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
              ) {
                Text(
                  text = "LinkXort", style = MaterialTheme.typography.titleLarge
                )
              }
            }, colors = TopAppBarDefaults.topAppBarColors().copy(
              containerColor = MaterialTheme.colorScheme.primary,
              titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
          Box(
            modifier = Modifier
              .padding(innerPadding)
              .fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            val state by viewModel.state.collectAsState()
            var showDialog by remember { mutableStateOf(false) }

            LaunchedEffect(state.message) {
              if (state.message.isNotBlank()) {
                showDialog = true
              }
            }

            HomePage(state = state, events = viewModel.events)

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
        }
      }
    }
  }
}
