package com.defined.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.defined.mobile.ui.theme.BackButton
import androidx.navigation.NavController
import com.defined.mobile.R
import com.defined.mobile.backend.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListPage(
    shoppingListViewModel: ShoppingListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
    onBackClick: () -> Unit,
    backActive: Boolean = true
) {
    val shoppingList by shoppingListViewModel.shoppingList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Üst bölüm: Geri butonu ve sayfa başlığı
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (backActive) {
                BackButton(onBackClick)
            }
            Text(
                text = "Shopping List",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Boşluk veya ayraç eklemek istersen:
        Spacer(modifier = Modifier.height(8.dp))

        // Alışveriş listesi boş mu?
        if (shoppingList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No ingredients in your shopping list.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(shoppingList) { ingredient ->
                    IngredientCard(
                        ingredient = ingredient,
                        isAvailable = false,
                        text = ingredient.name,
                        isInShoppingList = true,
                        onDeleteClick = { shoppingListViewModel.removeIngredient(ingredient) }
                    )
                }
            }
        }
    }
}
