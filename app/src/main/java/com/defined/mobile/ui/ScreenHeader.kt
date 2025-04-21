package com.defined.mobile.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.defined.mobile.ui.theme.*

@Composable
fun ScreenHeader(
    title: String,
    onNavigateBack: () -> Unit,
    rightContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back button
        BackButton(onNavigateBack = onNavigateBack)

        // Centered title (visually centered between two elements)
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Right-side content (like an add/save icon)
        if (rightContent != null) {
            rightContent()
        } else {
            Spacer(modifier = Modifier.size(24.dp)) // match IconButton size for symmetry
        }
    }
}
