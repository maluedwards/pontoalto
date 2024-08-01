package com.example.pontoalto.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.pontoalto.viewmodel.NewProjectViewModel
import com.example.pontoalto.viewmodel.event.NewProjectUiEvent

@Composable
fun NewProjectScreen(
    navController: NavHostController,
    newProjectViewModel: NewProjectViewModel,
    recipeName: String
) {

    val newProjectState = newProjectViewModel.uiState.collectAsState().value

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
        newProjectViewModel.onEvent(NewProjectUiEvent.UpdateRecipeName(recipeName))
    }


    PontoAltoTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(gradient)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = false, newRecipe = false, navController) },
            containerColor = Color.Transparent
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
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Recipe name text field
                        TextField(
                            value = newProjectState.projectName,
                            onValueChange = {
                                newProjectViewModel.onEvent(
                                    NewProjectUiEvent.UpdateProjectName(
                                        it
                                    )
                                )
                            },
                            label = { Text("Project Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.fillMaxWidth())
                        Column() {
                            Text(
                                text = "Recipe: ",
                                color = Color(0xFF5941A9),
                                fontSize = 22.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = newProjectState.recipeName, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        // Save Recipe Button
                        Button(
                            onClick = {
                                newProjectViewModel.onEvent(NewProjectUiEvent.SaveProject)
                                navController.navigate("home") {
                                    popUpTo("new-recipe") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF84CE)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Save Project")
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
}
