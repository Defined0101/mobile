package com.defined.mobile.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun UnsavedChangesDialog(
    onDismiss: () -> Unit,
    onConfirmLeave: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unsaved Changes") },
        text = { Text("You have unsaved changes. Are you sure you want to leave without saving?") },
        confirmButton = {
            TextButton(onClick = onConfirmLeave) {
                Text("Leave")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Stay")
            }
        }
    )
}
