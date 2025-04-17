package com.defined.mobile.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.R

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

@Composable
fun BackButton(onNavigateBack: () -> Unit){
    IconButton(
        onClick = onNavigateBack
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isEnabled, // this is still necessary
        modifier = modifier.padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(text = "Save", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit,
    contentDescription: String = "Delete"
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EditButton(onClick: () -> Unit, text: String = "Edit Profile") {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = fontMedium
        )
    }
}

@Composable
fun LikeButton(isLiked: Boolean, onClick: () -> Unit) {
    val iconColor = LocalContentColor.current
    IconButton(onClick = {
        onClick()
    }) {
        Image(
            painter = painterResource(id = if (isLiked) R.drawable.thumbs_up_solid else R.drawable.thumbs_up_regular),
            contentDescription = "Like",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}


@Composable
fun DislikeButton(isDisliked: Boolean, onClick: () -> Unit) {
    val iconColor = LocalContentColor.current
    IconButton(onClick = {
        onClick()
    }) {
        Image(
            painter = painterResource(id = if (isDisliked) R.drawable.dislike_fill else R.drawable.dislike_default),
            contentDescription = "Dislike",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}

@Composable
fun SaveRecipeButton(isSaved: Boolean, onClick: () -> Unit) {
    val iconColor = LocalContentColor.current
    var saved by remember { mutableStateOf(isSaved) }

    IconButton(onClick = {
        saved = !saved
        onClick()
    }) {
        Image(
            painter = painterResource(id = if (saved) R.drawable.bookmark_solid else R.drawable.bookmark_regular),
            contentDescription = "SaveRecipe",
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}

