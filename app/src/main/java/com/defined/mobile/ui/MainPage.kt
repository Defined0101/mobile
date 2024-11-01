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
    // Root container to display the main page content
    Box(
        modifier = Modifier
            .fillMaxSize() // Full screen size for main layout
    ) {
        // Background image for the main page
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Scales the image to fill the background
        )

        // Column layout to stack elements vertically on the main page
        Column(
            modifier = Modifier
                .fillMaxSize() // Full screen size for the column
                .padding(horizontal = 16.dp, vertical = 8.dp), // Adds padding around the column
            verticalArrangement = Arrangement.Top, // Align elements to the top
            horizontalAlignment = Alignment.CenterHorizontally // Center-align horizontally
        ) {
            TopBar(onSearchClick) // Top bar with search functionality
            Spacer(modifier = Modifier.height(12.dp)) // Space between top bar and featured image
            FeaturedImage() // Displays a featured image in a card layout
            Spacer(modifier = Modifier.height(12.dp))
            CategorySection() // Section showing categories
            Spacer(modifier = Modifier.height(12.dp))
            RecipeSection() // Section showing recipes
        }
    }
}

/* TODO: When clicked on search box, application should navigate to SearchScreen
*  Now user needs to click on search button on keyboard.
* */
@Composable
fun ClickableOutlinedTextField(
    onClick: () -> Unit
) {
    // State to store the text input
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { newText -> text = newText }, // Updates text state on input change
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search") // Search icon inside text field
        },
        placeholder = {
            Text("Search", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }, // Placeholder text
        modifier = Modifier
            .fillMaxWidth() // Full width of the screen
            .height(56.dp), // Standard height for consistency
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface, // Background color when unfocused
            unfocusedBorderColor = MaterialTheme.colorScheme.primary, // Border color when unfocused
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Border color when focused
            cursorColor = MaterialTheme.colorScheme.primary, // Cursor color
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search // Sets action to Search on keyboard
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onClick() // Trigger search action when search key is pressed
            }
        )
    )
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
            .height(200.dp), // Fixed height
        shape = MaterialTheme.shapes.medium, // Rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(8.dp) // Shadow effect for depth
    ) {
        Image(
            painter = painterResource(id = R.drawable.featured_image),
            contentDescription = "Featured Image",
            contentScale = ContentScale.Fit, // Fit image within card without cropping
            modifier = Modifier.fillMaxSize()
        )
    }
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
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Divider(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // Slightly transparent divider
        thickness = 1.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    // Horizontal list of category items
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp), // Padding for content
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
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
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Divider(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        thickness = 1.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    // Vertical list of recipe items
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp), // Padding for content
        verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
    ) {
        items(10) { index ->
            RecipeItem("Recipe Name $index") // Placeholder recipe item
        }
    }
}
