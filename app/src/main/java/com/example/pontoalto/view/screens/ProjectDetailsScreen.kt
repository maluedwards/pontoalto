package com.example.pontoalto.view.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.ProjectDetailsViewModel
import com.example.pontoalto.viewmodel.StitchRowViewModel
import com.example.pontoalto.viewmodel.event.ProjectDetailsUiEvent

@Composable
fun ProjectDetailsScreen(
    navController: NavHostController,
    projectViewModel: ProjectDetailsViewModel,
    projectName: String,
    stitchRowViewModel: StitchRowViewModel
) {
    val projectState by projectViewModel.uiState.collectAsState()

    val stitchRowsState by stitchRowViewModel.stitchRows.collectAsState()

    LaunchedEffect(projectState.recipeName) {
        Log.d("RecipeDetailsScreen", "Loading recipe details for: $projectState.recipeName")
        stitchRowViewModel.loadStitchRows(projectState.recipeName) // Load stitch rows for the given recipe
    }

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = false, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) { innerPadding ->

            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "Project Name: ${projectState.projectName}")
                    Text(text = "Recipe Name: ${projectState.recipeName}")
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Stitch Rows:")
                    LazyColumn {
                        items(stitchRowsState) { row ->
                            Text(text = "Row ${row.rowNumber}: ${row.instructions} (${row.stitches} stitches)")
                        }
                    }

                    Text(text = "Current Stitch: ${projectState.currentStitch}")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { projectViewModel.onEvent(ProjectDetailsUiEvent.DecrementStitch(1)) }) {
                            Text(text = "-")
                        }
                        Button(onClick = { projectViewModel.onEvent(ProjectDetailsUiEvent.IncrementStitch(1)) }) {
                            Text(text = "+")
                        }
                    }
                }
            }
        }
    }
}
