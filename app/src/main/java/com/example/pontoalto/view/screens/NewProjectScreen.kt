package com.example.pontoalto.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.NewProjectViewModel
import com.example.pontoalto.viewmodel.event.NewProjectUiEvent

@Composable
fun NewProjectScreen(
    navController: NavHostController,
    newProjectViewModel: NewProjectViewModel,
    recipeName: String
) {

    val newProjectState = newProjectViewModel.uiState.collectAsState().value

    LaunchedEffect(recipeName) {
        newProjectViewModel.onEvent(NewProjectUiEvent.UpdateRecipeName(recipeName))
    }


    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = false, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxSize(0.98f)
                ) {
                    Text(
                        text = "New Project",
                        modifier = Modifier.padding(8.dp)
                    )
                    HorizontalDivider()
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Recipe name text field
                        TextField(
                            value = newProjectState.projectName,
                            onValueChange = { newProjectViewModel.onEvent(NewProjectUiEvent.UpdateProjectName(it)) },
                            label = { Text("Project Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.fillMaxWidth())

                        Text(text = "Recipe Name: ${newProjectState.recipeName}")
                        // Save Recipe Button
                        Button(
                            onClick = { newProjectViewModel.onEvent(NewProjectUiEvent.SaveProject)
                                navController.navigate("home") {
                                    popUpTo("new-recipe") { inclusive = true }
                                } },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Save Project")
                        }

                        if (newProjectState.error != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = newProjectState.error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                }
            }
        }
    }
}
