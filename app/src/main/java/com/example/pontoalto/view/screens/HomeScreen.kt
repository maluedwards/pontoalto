@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ReceitaHor
import com.example.pontoalto.ReceitaVer
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.repository.RecipeRepository
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(navController: NavHostController) {

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar( listRecipes = false, home = true, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
        { innerPadding ->
            Column (
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.98.toFloat())
                ) {
                    Text(text = "Current Projects",
                        modifier = Modifier.padding(8.dp))
                    Row(Modifier
                        .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        ReceitaHor()
                        ReceitaHor()
                    }
                }

            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, navController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Handle recipe click, navigate to recipe details
                navController.navigate("recipe_detail_screen/${recipe.recipeName}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = recipe.recipeName, style = MaterialTheme.typography.titleMedium)
            // Optionally, show more details about the recipe here
        }
    }
}


