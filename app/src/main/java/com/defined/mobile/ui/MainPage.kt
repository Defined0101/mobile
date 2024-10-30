package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defined.mobile.R

@Composable
fun MainPage(onSearchClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(onSearchClick)
            Spacer(modifier = Modifier.height(12.dp))
            FeaturedImage()
            Spacer(modifier = Modifier.height(12.dp))
            CategorySection()
            Spacer(modifier = Modifier.height(12.dp))
            RecipeSection()
        }
    }
}

@Composable
fun ClickableOutlinedTextField(
    onClick: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { newText -> text = newText },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },
        placeholder = { Text("Search", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), // Standard height for better UI consistency
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onClick() // Trigger search action on IME Search key
            }
        )
    )
}

@Composable
fun TopBar(onSearchClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 4.dp)
        )
        ClickableOutlinedTextField(onSearchClick)
    }
}

@Composable
fun FeaturedImage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.featured_image),
            contentDescription = "Featured Image",
            contentScale = ContentScale.Fit, // Adjust the scale to fit the image without cropping
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CategorySection() {
    Text(
        text = "Categories",
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
    )
    Divider(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        thickness = 1.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            CategoryItem("Sample $index")
        }
    }
}

@Composable
fun RecipeSection() {
    Text(
        text = "Recipes",
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
    )
    Divider(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        thickness = 1.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            RecipeItem("Recipe Name $index")
        }
    }
}
