package com.defined.mobile.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


// Data class representing each item in the bottom navigation bar
data class BottomNavigationBarItem(
    val title: String, // Title to be displayed for the item
    val selectedIcon: ImageVector, // Icon to show when the item is selected
    val unselectedIcon: ImageVector // Icon to show when the item is unselected
)

@Composable
fun BottomNavBar(
    items: List<BottomNavigationBarItem>, // List of items to display in the navigation bar
    onItemSelected: (Int) -> Unit, // Callback to handle item selection, providing the index
    selectedIndex: Int // Currently selected item's index
){
    // Navigation bar containing multiple items
    NavigationBar {
        // Iterate through the items with their index
        items.forEachIndexed { index, item ->
            // Each item in the navigation bar
            NavigationBarItem(
                selected = selectedIndex == index, // Check if this item is the currently selected one
                onClick = {
                    onItemSelected(index) // Trigger the item selection callback with the item's index
                },
                label = {
                    Text(text = item.title) // Display the item's title as a label
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedIndex) {
                            item.selectedIcon // Show selected icon if this item is selected
                        } else item.unselectedIcon, // Otherwise, show unselected icon
                        contentDescription = item.title // Accessibility description for the icon
                    )
                }
            )
        }
    }
}

// Function to create and return the list of navigation items
fun getBottomNavItems(): List<BottomNavigationBarItem> {
    return listOf(
        BottomNavigationBarItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationBarItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
        ),
        BottomNavigationBarItem(
            title = "Favs",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
        ),
        BottomNavigationBarItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
        )
    )
}
