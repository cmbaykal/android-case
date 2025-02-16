package com.mrbaikal.nesineandroidcase.base.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrbaikal.nesineandroidcase.ext.message
import com.mrbaikal.nesineandroidcase.ext.progress
import com.mrbaikal.nesineandroidcase.ext.showMessage

@Composable
fun ScreenHandler(viewModel: ViewModel) {
    val progress by viewModel.progress.collectAsStateWithLifecycle(false)
    val error by viewModel.message.collectAsStateWithLifecycle(null)

    if (progress) {
        Dialog({}, DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    if (!error.isNullOrEmpty()) {
        AlertDialog(
            text = { Text(text = error.orEmpty()) },
            onDismissRequest = { viewModel.showMessage(null) },
            confirmButton = {
                Button(
                    onClick = { viewModel.showMessage(null) }
                ) {
                    Text(text = "Tamam")
                }
            }
        )
    }
}

@Composable
fun BaseScreen(viewModel: ViewModel, content: @Composable () -> Unit) {
    content()
    ScreenHandler(viewModel)
}