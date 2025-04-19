package com.defined.mobile.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ShoppingCart
import com.composables.icons.lucide.AlarmClock
import com.composables.icons.lucide.ReceiptText
import com.composables.icons.lucide.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Fish
import com.composables.icons.lucide.Hop
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MilkOff
import com.composables.icons.lucide.Vegan
import com.composables.icons.lucide.WheatOff
import com.composables.icons.lucide.Zap
import com.defined.mobile.R
import java.util.Locale
import com.defined.mobile.ui.theme.*

// Recipe data class
data class RecipeDetail(
    val name: String,
    val instructions: String,
    val ingredients: List<String>,
    val dietPreferences: List<String>,
    val category: String,
    val totalTime: String,
    val totalCalories: String
)

// Dummy recipe data. When calling the recipe page, the recipe with the submitted index will be shown.
val dummyRecipes = listOf(
    RecipeDetail(
        name = "Go Oregonian! Oregon Style Pork Chops with Pinot Noir and Cranberries, Oregon Hash with Wild Mushrooms, Greens, Beets, Hazelnuts and Blue Cheese, Charred Whole Grain Bread with Butter and Chives",
        instructions = "Mix flour, sugar, cocoa powder, eggs and butter. Pour into a greased pan and bake at 350°F for 45 minutes. Mix flour, sugar, cocoa powder, eggs and butter. Pour into a greased pan and bake at 350°F for 45 minutes. Mix flour, sugar, cocoa powder, eggs and butter. Pour into a greased pan and bake at 350°F for 45 minutes",
        ingredients = listOf("Flour", "Sugar", "Cocoa Powder", "Eggs", "Butter"),
        dietPreferences = listOf("pescetarian", "gluten free", "vegetarian"),
        category = "Dessert",
        totalTime = "45 min",
        totalCalories = "350 kcal"
    ),
    RecipeDetail(
        name = "Apple Pie",
        instructions = "Slice apples, mix with cinnamon, sugar and butter. Place in a pastry-lined pan and bake for 60 minutes.",
        ingredients = listOf("Apples", "Flour", "Sugar", "Butter", "Cinnamon"),
        dietPreferences = listOf("vegetarian", "gluten free"),
        category = "Dessert",
        totalTime = "60 min",
        totalCalories = "400 kcal"
    ),
    RecipeDetail(
        name = "BBQ Chicken",
        instructions = "Season chicken breasts with salt and brush with BBQ sauce. Grill until fully cooked.",
        ingredients = listOf("Chicken breasts", "Salt", "BBQ sauce"),
        dietPreferences = listOf("gluten free"),
        category = "Main Course",
        totalTime = "30 min",
        totalCalories = "500 kcal"
    )
)

// Special icons selection according to diet preferences.
fun getDietIcon(preference: String): ImageVector {
    return when (preference.lowercase(Locale.getDefault())) {
        "vegan" -> Lucide.Vegan
        "vegetarian" -> Lucide.Hop
        "dairy free" -> Lucide.MilkOff
        "gluten free" -> Lucide.WheatOff
        "pescetarian" -> Lucide.Fish
        else -> Lucide.Leaf
    }
}

// Special background color for chip according to diet preferences.
fun getDietChipColor(preference: String): Color {
    return when (preference.lowercase(Locale.getDefault())) {
        "vegan" -> VeganColor
        "vegetarian" -> VegetarianColor
        "dairy free" -> DairyFreeColor
        "gluten free" -> GlutenFreeColor
        "pescetarian" -> PescetarianColor
        else -> DefaultChipColor
    }
}

// Filter diet preferences to prevent redundancy.
fun filterDietPreferences(preferences: List<String>?): List<String> {
    if (preferences == null) return emptyList()
    val filtered = preferences.filter { it.lowercase(Locale.getDefault()) != "meat" }
    if (filtered.any { it.lowercase(Locale.getDefault()) == "vegan" }) {
        return filtered.filter {
            val lower = it.lowercase(Locale.getDefault())
            lower != "vegetarian" && lower != "pescetarian"
        }
    }
    if (filtered.any { it.lowercase(Locale.getDefault()) == "vegetarian" } &&
        filtered.any { it.lowercase(Locale.getDefault()) == "pescetarian" }) {
        return filtered.filter { it.lowercase(Locale.getDefault()) != "pescetarian" }
    }
    return filtered
}

// Show diet preferences.
@Composable
fun DietPreferenceChip(
    preference: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = getDietChipColor(preference)
    val contentColor = MaterialTheme.colorScheme.onBackground

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        tonalElevation = 2.dp,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = getDietIcon(preference),
                contentDescription = preference,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = preference.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.bodySmall,
                color = contentColor
            )
        }
    }
}

// InfoBadge: Show category, time and calori information.
@Composable
fun InfoBadge(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {


    Surface(
        shape = fullyRounded,
        color = badgeBackground,
        tonalElevation = 2.dp,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = badgeContentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = badgeContentColor
            )
        }
    }
}

// ExpandableSection: Content can be switched on and off.
@Composable
fun ExpandableSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    backgroundColor: Color,
    scrollable: Boolean = false,
    leadingIcon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotation),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                if (scrollable) {
                    Box(
                        modifier = Modifier
                            .heightIn(max = 150.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    }
}

// ExpandableRecipeName: In the closed state it is shown on a single line with ellipsis; clicking on it opens the full text.
// A common font style (headlineSmall, Bold) is used in closed and open state.
// When closed, the text is single line; when expanded, the text is justified.
@Composable
fun ExpandableRecipeName(recipeName: String) {
    var expanded by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = BoldWeight)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = recipeName,
            style = textStyle,
            color = softBlue, // Soft blue
            maxLines = if (expanded) Int.MAX_VALUE else 1,
            overflow = if (expanded) TextOverflow.Clip else TextOverflow.Ellipsis,
            textAlign = if (expanded) TextAlign.Justify else TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = if (expanded) "Collapse" else "Expand",
            tint = softBlue,
            modifier = Modifier
                .size(24.dp)
                .rotate(if (expanded) 180f else 0f)
        )
    }
}

@Composable
fun RecipePage(recipeId: String, onBackClick: () -> Unit) {
    val index = recipeId.toIntOrNull() ?: 0
    val recipe = dummyRecipes.getOrNull(index)

    var ingredientsExpanded by remember { mutableStateOf(false) }
    var instructionsExpanded by remember { mutableStateOf(false) }

    // Translucent overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: The line with t
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBackClick)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (recipe != null) {
            // Recipe name: Displayed using ExpandableRecipeName.
            ExpandableRecipeName(recipeName = recipe.name)
            Spacer(modifier = Modifier.height(8.dp))
            // Recipe photo and section showing dietary preferences.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.recipe_sample),
                        contentDescription = "Recipe Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
                // Dietary preferences are shown if the filtered list is not empty.
                val filteredPreferences = filterDietPreferences(recipe.dietPreferences)
                if (filteredPreferences.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
                            .padding(8.dp)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        filteredPreferences.forEach { preference ->
                            DietPreferenceChip(preference = preference)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoBadge(
                    icon = Lucide.Tag,
                    label = recipe.category,
                    modifier = Modifier.weight(1f)
                )
                InfoBadge(
                    icon = Lucide.AlarmClock,
                    label = recipe.totalTime,
                    modifier = Modifier.weight(1f)
                )
                InfoBadge(
                    icon = Lucide.Zap,
                    label = recipe.totalCalories,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ExpandableSection(
                title = "What do I need?",
                expanded = ingredientsExpanded,
                onToggle = { ingredientsExpanded = !ingredientsExpanded },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                scrollable = true,
                leadingIcon = Icons.Filled.ShoppingCart,
                content = {
                    Column {
                        recipe.ingredients.forEach { ingredient ->
                            Text(
                                text = "• $ingredient",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExpandableSection(
                title = "How can I do that?",
                expanded = instructionsExpanded,
                onToggle = { instructionsExpanded = !instructionsExpanded },
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                scrollable = true,
                leadingIcon = Lucide.ReceiptText,
                content = {
                    Text(
                        text = recipe.instructions,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            )
        } else {
            Text(
                text = "Recipe not found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        tonalElevation = 4.dp,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = BoldWeight),
                color = contentColor
            )
        }
    }
}
