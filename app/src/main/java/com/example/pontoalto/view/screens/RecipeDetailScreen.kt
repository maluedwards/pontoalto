package com.example.pontoalto.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.R
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
        Box(modifier = Modifier
            .fillMaxSize()
            .background(gradient)) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MyHeader() },
                bottomBar = {
                    MyNavBar(
                        listRecipes = false,
                        home = false,
                        newRecipe = false,
                        navController
                    )
                },
                containerColor = Color.Transparent
            )
            { innerPadding ->

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp)
                ) {
                    Column (modifier = Modifier.padding(20.dp)){
                        recipe?.let {
                            Text(text = stringResource(id = R.string.recipe_name) + ": ${it.recipe.recipeName}",
                                fontFamily = customFont,
                                fontSize = 24.sp,
                                color = Color(0xFFFF84CE))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = stringResource(id = R.string.difficulty) + ": ${it.recipe.difficulty}",
                                fontFamily = customFont,
                                fontSize = 18.sp,
                                color = Color(0xFFFF84CE))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = stringResource(id = R.string.row_stitch) + ":",
                            fontSize = 20.sp,
                            color = Color(0xFF5941A9))
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn {
                            items(stitchRowsState) { row ->
                                Text(text = stringResource(id = R.string.row) + " ${row.rowNumber}: ${row.instructions} (${row.stitches} "
                                + stringResource(id = R.string.row_stitches) + ")")
                            }
                        } ?: run {
                            Text(text = "Loading stitch rows...")
                        }

                        Button(
                            onClick = {
                                navController.navigate("new-project/${recipeName}")
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF84CE)),
                            shape = RoundedCornerShape(8.dp)

                        ) {
                            Text(text = stringResource(id = R.string.project_add_new))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                Log.d(
                                    "RecipeDetailsScreen",
                                    "Deleting recipe: ${recipe?.recipe?.recipeName}"
                                )
                                recipeViewModel.deleteRecipe(recipe?.recipe?.recipeName ?: "")
                                navController.navigate("recipes") {
                                    popUpTo("recipe-detail") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF84CE)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = stringResource(id = R.string.recipe_delete))
                        }
                    }
                }
            }
        }
    }
}


