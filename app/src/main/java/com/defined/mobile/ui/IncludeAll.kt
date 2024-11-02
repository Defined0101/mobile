package com.defined.mobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

//import com.example.defined.ProfileScreen
//import com.example.defined.BottomNavBar

@Composable
fun ScreenWithBottomNav() {
    // List of navigation items for the bottom navigation bar
    val items = getBottomNavItems()

    // State to keep track of the selected item index in the bottom navigation bar
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    // Scaffold layout with a bottom navigation bar and main content area
    Scaffold(
        bottomBar = {
            // BottomNavBar composable with a list of items, selected index, and a selection callback
            BottomNavBar(
                items = items, // List of navigation items
                onItemSelected = { index ->
                    selectedItemIndex = index // Update the selected index when an item is clicked
                },
                selectedIndex = selectedItemIndex// Currently selected item index
            )
        },
        content = { paddingValues ->
            // Main content area of the Scaffold, with padding to account for the bottom bar
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Content to be displayed above the bottom navigation bar
                //ProfileScreen()
            }
        }
    )
}