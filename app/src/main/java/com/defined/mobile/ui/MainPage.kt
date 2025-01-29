package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defined.mobile.R

@Composable
fun MainPage(onSearchClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp), // Instead of individual Spacer
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(onSearchClick)
            // FeaturedImage()
            CategorySection()
            RecipeSection()
        }
    }
}

@Composable
fun ClickableOutlinedTextField(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() } // Trigger navigation to SearchScreen
            .background(MaterialTheme.colorScheme.surface) // Background for click area
            .padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
    }
}


@Composable
fun TopBar(onSearchClick: () -> Unit) {
    // Column layout for top bar with a welcome message and search field
    Column(
        modifier = Modifier.fillMaxWidth() // Full width
    ) {
        // Welcome text
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 4.dp) // Padding below text
        )
        ClickableOutlinedTextField(onSearchClick) // Search field with click action
    }
}

@Composable
fun FeaturedImage() {
    // Card component to display a featured image
    Card(
        modifier = Modifier
            .fillMaxWidth() // Full width card
            .height(150.dp), // Fixed height
        shape = MaterialTheme.shapes.medium, // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(8.dp) // Shadow effect for depth
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Featured Image",
            contentScale = ContentScale.Fit, // Fit image within card without cropping
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MainPageDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun CategorySection() {
    // Title for category section
    Text(
        text = "Categories",
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 6.dp)
    )
    MainPageDivider()
    // Horizontal list of category items
    LazyRow(
        contentPadding = PaddingValues(horizontal = 6.dp), // Padding for content
        horizontalArrangement = Arrangement.spacedBy(6.dp) // Spacing between items
    ) {
        items(10) { index ->
            CategoryItem("Sample $index") // Placeholder category item
        }
    }
}

@Composable
fun RecipeSection() {
    // Title for recipe section
    Text(
        text = "Recipes",
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 6.dp)
    )
    MainPageDivider()
    // Vertical list of recipe items
    LazyColumn(
        contentPadding = PaddingValues(vertical = 6.dp), // Padding for content
        verticalArrangement = Arrangement.spacedBy(6.dp) // Spacing between items
    ) {
        items(10) { index ->
            RecipeItem("Recipe Name $index") // Placeholder recipe item
        }
    }
}
