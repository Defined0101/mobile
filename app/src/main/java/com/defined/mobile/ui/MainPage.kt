package com.defined.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.defined.mobile.R
import com.defined.mobile.backend.CategoryViewModel
import com.defined.mobile.backend.RecipeViewModel
import com.defined.mobile.backend.ShoppingListViewModel
import com.defined.mobile.ui.theme.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@Composable
fun MainPage(recipeViewModel: RecipeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
             categoryViewModel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
             shoppingListViewModel: ShoppingListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
             loginViewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
             navController: NavController,
             onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), // Instead of individual Spacer
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(onSearchClick)
        loginViewModel.currentUser()?.let { SurpriseMeButton(recipeViewModel = recipeViewModel, navController = navController, userId = it.uid) }
        // FeaturedImage()
        CategorySection(viewModel = categoryViewModel, navController = navController)
        RecipeSection(navController, recipeViewModel)
    }
}

@Composable
fun SurpriseMeButton(
    recipeViewModel: RecipeViewModel,
    navController: NavController,
    userId: String
) {
    // 1) “go” flag’ini remember ile tut, mutableState olduğuna dikkat et
    var go by remember { mutableStateOf(false) }
    // 2) sürpriz ID’yi koleksiyonla al
    val surpriseRecipeId by recipeViewModel
        .surpriseRecipe
        .collectAsState(initial = 0)

    Button(
        onClick = {
            // 3) butona her basıldığında önce fetch'i tetikle
            recipeViewModel.fetchSurpriseRecipeId(userId)
            // 4) sonra go flag’ini true yap
            go = true
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Surprise Me")
    }

    // 5) go state’i değiştiğinde bu effect çalışır
    LaunchedEffect(go) {
        if (go) {
            // 6) Eğer ID henüz 0’sa, emitlenmesini bekle
            val id = if (surpriseRecipeId != 0) {
                surpriseRecipeId
            } else {
                snapshotFlow { surpriseRecipeId }
                    .filter { it != 0 }
                    .first()      // suspend; ilk non-zero değeri al
            }
            // 7) navigasyonu yap
            navController.navigate("recipePage/$id")
            // 8) flag’i sıfırla ki bir sonraki tıklamada yeniden çalışsın
            go = false
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
                color = MaterialTheme.colorScheme.primary
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
fun CategorySection(viewModel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    val categories by viewModel.categories.collectAsState()

    Text(
        text = "Categories",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = fontLarge, color = MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(vertical = 6.dp)
    )
    MainPageDivider()
    LazyRow(
        contentPadding = PaddingValues(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(categories) { category ->
            // Güncellenmiş CategoryItem: kategori adı ve tıklama callback'i alıyor.
            CategoryItem(name = category, onClick = {
                // Kategoriye tıklandığında, SearchScreen'e geçiş yapılırken seçili kategori de argüman olarak gönderiliyor.
                navController.navigate("search/true/${category}")
            })
        }
    }
}

@Composable
fun RecipeSection(navController: NavController, viewModel: RecipeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val recipes by viewModel.recipes.collectAsState()
    val recipeCard by viewModel.recipeCard.collectAsState()
    var selectedRecipeId by remember { mutableStateOf<Int?>(null) } // Track selected recipe

    // Title for recipe section
    Text(
        text = "Recipes",
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = fontLarge,
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
        items(recipes) { recipe ->
            RecipeItem(
                recipe= recipe,
                onClick = {
                    navController.navigate("recipePage/${recipe.ID}")
                }
            )
        }
    }
}
