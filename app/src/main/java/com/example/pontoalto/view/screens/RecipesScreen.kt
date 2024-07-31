@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.R
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.RecipeViewModel

@Composable
fun RecipesScreen(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel
) {
    // Collect the recipes from the ViewModel
    val recipes by recipeViewModel.recipes.collectAsState()

    val gradient = Brush.linearGradient(
        0.1f to Color(0xFFB685E8),
        0.1f to Color(0xFFB685E8),
        1.0f to Color(0xFFFFFFFF),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val customFont = FontFamily(
        Font(R.font.poetsen_one, FontWeight.Normal)
    )

    PontoAltoTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(gradient)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = true, home = false, newRecipe = false, navController) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ElevatedCard(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Recipes",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(recipe = recipe, onClick = {
                            navController.navigate("recipe-detail/${recipe.recipeName}")
                        })
                        HorizontalDivider()
                    }
                }
            }
        }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = recipe.recipeName, style = MaterialTheme.typography.titleMedium)
            Text(text = "Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodyMedium)
        }

    }
}