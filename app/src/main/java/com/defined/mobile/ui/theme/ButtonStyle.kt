package com.defined.mobile.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun StyledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 4.dp)
            .shadow(6.dp, MaterialTheme.shapes.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary, // Uses primary color from theme
            contentColor = MaterialTheme.colorScheme.onPrimary // Uses onPrimary for text color
        ),
        shape = MaterialTheme.shapes.medium // Shape as defined in theme
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge // Uses theme-defined typography
        )
    }
}
