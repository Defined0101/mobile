package com.defined.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.defined.mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInformation(viewModel: LoginViewModel, onNavigateBack: () -> Unit) {
    val currentUser = viewModel.currentUser();

    var name by rememberSaveable { mutableStateOf(currentUser?.displayName) }
//    var birthday by rememberSaveable { mutableStateOf("Birthday") }
    var phoneNumber by rememberSaveable { mutableStateOf(currentUser?.phoneNumber) }
    var email by rememberSaveable { mutableStateOf(currentUser?.email) }
//    var password by rememberSaveable { mutableStateOf("Password") }

    Scaffold(
        containerColor = TransparentColor,
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile Information", color = MaterialTheme.colorScheme.onSecondary) },
                navigationIcon = {
                    BackButton(onNavigateBack)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TransparentColor, // Remove default background
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Adjust text color
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Adjust icon color
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                name?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = fontLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information rows
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                ProfileInfoRow(Icons.Default.DateRange, "Birthday", birthday, onValueChange = { birthday = it })
                phoneNumber?.let { it ->
                    ProfileInfoRow(Icons.Default.Phone, "Phone Number",
                        it, onValueChange = { phoneNumber = it })
                }
                email?.let {
                    ProfileInfoRow(Icons.Default.Email, "Email",
                        it, onValueChange = { email = it })
                }
//                 ProfileInfoRow(Icons.Default.Lock, "Password", password, onValueChange = { password = it })
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom button with gradient
//            EditButton(onClick = onSave)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String, onValueChange: (String) -> Unit) {
    val shape = MaterialTheme.shapes.medium // Define a more rounded shape
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Rounded background with TextField
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(shape) // Apply rounded corners
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // Add a noticeable background color
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding inside the rounded box
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = label) }, // Add placeholder for clarity
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = TransparentColor, // Make the container transparent
                    focusedIndicatorColor = TransparentColor, // Remove underline
                    unfocusedIndicatorColor = TransparentColor
                ),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                readOnly = true
            )
        }
    }
}









