import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInformation(onNavigateBack: () -> Unit, onSave: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("Name") }
    var birthday by rememberSaveable { mutableStateOf("Birthday") }
    var phoneNumber by rememberSaveable { mutableStateOf("818 123 4567") }
    var email by rememberSaveable { mutableStateOf("info@aplusdesign.co") }
    var password by rememberSaveable { mutableStateOf("Password") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile Information") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top profile section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF6A1B9A), Color(0xFF8E24AA))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = name, color = Color.White, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information rows
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ProfileInfoRow(Icons.Default.DateRange, "Birthday", birthday, onValueChange = { birthday = it })
                ProfileInfoRow(Icons.Default.Phone, "Phone Number", phoneNumber, onValueChange = { phoneNumber = it })
                ProfileInfoRow(Icons.Default.Email, "Email", email, onValueChange = { email = it })
                ProfileInfoRow(Icons.Default.Lock, "Password", password, onValueChange = { password = it })
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom button with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp) // Increased height
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6A1B9A), Color(0xFF8E24AA))
                        ),
                        shape = RoundedCornerShape(30.dp) // Slightly more rounded
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Edit Profile",
                    color = Color.White,
                    fontSize = 18.sp, // Increased font size
                    modifier = Modifier.clickable(onClick = onSave)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String, onValueChange: (String) -> Unit) {
    val shape = RoundedCornerShape(16.dp) // Define a more rounded shape
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
                placeholder = { Text(text = label, color = Color.Gray) }, // Add placeholder for clarity
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Make the container transparent
                    focusedIndicatorColor = Color.Transparent, // Remove underline
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}









