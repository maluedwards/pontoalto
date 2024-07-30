@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
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

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = true, home = false, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) { innerPadding ->
            ElevatedCard(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(text = "Recipes",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleLarge)
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
                            // Handle recipe click, e.g., navigate to recipe details
                            //navController.navigate("recipe_detail/${recipe.}")
                        })
                        HorizontalDivider()
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