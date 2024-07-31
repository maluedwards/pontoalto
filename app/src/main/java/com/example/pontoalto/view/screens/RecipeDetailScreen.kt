package com.example.pontoalto.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.RecipeDetailsViewModel
import com.example.pontoalto.viewmodel.StitchRowViewModel

@Composable
fun RecipeDetailsScreen(
    navController: NavHostController,
    recipeName: String,
    recipeViewModel: RecipeDetailsViewModel,
    stitchRowViewModel: StitchRowViewModel
) {
    val recipe by recipeViewModel.recipeWithRows.collectAsState()
    val stitchRowsState by stitchRowViewModel.stitchRows.collectAsState()

    LaunchedEffect(recipeName) {
        Log.d("RecipeDetailsScreen", "Loading recipe details for: $recipeName")
        recipeViewModel.loadRecipeDetails(recipeName) // Method to load recipe details by ID
        stitchRowViewModel.loadStitchRows(recipeName) // Load stitch rows for the given recipe
    }

    LaunchedEffect(recipe) {
        Log.d("RecipeDetailsScreen", "Loaded recipe: $recipe")
    }

    LaunchedEffect(stitchRowsState) {
        Log.d("RecipeDetailsScreen", "Loaded stitch rows: $stitchRowsState")
    }

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = true, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
        { innerPadding ->

            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Column {
                    recipe?.let {
                        Text(text = "Recipe Name: ${it.recipe.recipeName}")
                        Text(text = "Difficulty: ${it.recipe.difficulty}")
                    }

                    Text(text = "Stitch Rows:")
                    LazyColumn {
                        items(stitchRowsState) { row ->
                            Text(text = "Row ${row.rowNumber}: ${row.instructions} (${row.stitches} stitches)")
                        }
                    } ?: run {
                        Text(text = "Loading stitch rows...")
                    }

                    Button(
                        onClick = {
                            navController.navigate("new-project/${recipeName}")
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Add New Project")
                    }

                    Button(
                        onClick = {
                            Log.d("RecipeDetailsScreen", "Deleting recipe: ${recipe?.recipe?.recipeName}")
                            recipeViewModel.deleteRecipe(recipe?.recipe?.recipeName ?: "")
                            navController.navigate("recipes") {
                                popUpTo("recipe-detail") { inclusive = true }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Delete Recipe")
                    }
                    Log.d("RecipeDetailsScreen", "Recipe details: $recipe")
                }
            }
        }
    }
}


