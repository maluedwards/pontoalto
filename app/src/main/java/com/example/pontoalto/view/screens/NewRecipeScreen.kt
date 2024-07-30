package com.example.pontoalto.view.screens

import android.util.Log
import android.view.Menu
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.room.BuiltInTypeConverters
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.NewRecipeViewModel
import com.example.pontoalto.viewmodel.NewStitchRowViewModel
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.event.NewStitchRowUiEvent
import com.example.pontoalto.viewmodel.state.NewRecipeState
import com.example.pontoalto.viewmodel.state.NewStitchRowState

@Composable
fun NewRecipeScreen(
    navController: NavHostController,
    newRecipeViewModel: NewRecipeViewModel,
    newStitchRowViewModel: NewStitchRowViewModel
) {
    val newRecipeState = newRecipeViewModel.uiState.collectAsState().value
    val newStitchRowState = newStitchRowViewModel.uiState.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current

    // Navigate to the home screen if the recipe has been registered
    LaunchedEffect(newRecipeState.isRegistered) {
        Log.d("NewRecipeScreen", "isRegistered: ${newRecipeState.isRegistered}")
        if (newRecipeState.isRegistered) {
            navController.navigate("home") {
                popUpTo("new-recipe") { inclusive = true }
            }
        }
    }

    Layout(
        newRecipeState = newRecipeState,
        newRecipeViewModel = newRecipeViewModel,
        newStitchRowState = newStitchRowState,
        newStitchRowViewModel = newStitchRowViewModel,
        keyboardController = keyboardController,
        navController = navController
    )
}

@Composable
fun Layout(
    newRecipeState: NewRecipeState,
    newRecipeViewModel: NewRecipeViewModel,
    newStitchRowState: NewStitchRowState,
    newStitchRowViewModel: NewStitchRowViewModel,
    keyboardController: SoftwareKeyboardController?,
    navController: NavHostController
) {
    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = false, newRecipe = true, navController) },
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
                        text = "New Recipe",
                        modifier = Modifier.padding(8.dp)
                    )
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Recipe name text field
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = newRecipeState.recipeName,
                            onValueChange = { newRecipeViewModel.onEvent(NewRecipeUiEvent.UpdateRecipeName(it)) },
                            placeholder = { Text("Recipe Name") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                            ),
                        )
                        Spacer(modifier = Modifier.fillMaxWidth())

                        // Difficulty menu
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopStart)
                                .padding(6.dp)
                        ) {
                            var expand by remember { mutableStateOf(false) }
                            var difficulty by remember { mutableStateOf("Difficulty") }

                            OutlinedButton(onClick = { expand = true }, modifier = Modifier.padding(6.dp)) {
                                
                                Text(text = difficulty)
                            }

                            DropdownMenu(
                                expanded = expand,
                                onDismissRequest = { expand = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Advanced") },
                                    onClick = {
                                        newRecipeViewModel.onEvent(NewRecipeUiEvent.UpdateDifficulty(3))
                                        difficulty = "Advanced"
                                        expand = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Intermediate") },
                                    onClick = {
                                        newRecipeViewModel.onEvent(NewRecipeUiEvent.UpdateDifficulty(2))
                                        difficulty = "Intermediate"
                                        expand = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Basic") },
                                    onClick = {
                                        newRecipeViewModel.onEvent(NewRecipeUiEvent.UpdateDifficulty(1))
                                        difficulty = "Basic"
                                        expand = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxWidth())
                        HorizontalDivider()

                        // Stitch rows section
                        Column {
                            NewRow(
                                newStitchRowState = newStitchRowState,
                                newStitchRowViewModel = newStitchRowViewModel,
                                keyboardController = keyboardController
                            )
                        }

                        // Save Recipe Button
                        Button(
                            onClick = { newRecipeViewModel.onEvent(NewRecipeUiEvent.SaveRecipe) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Save Recipe")
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun NewRow(
    newStitchRowState: NewStitchRowState,
    newStitchRowViewModel: NewStitchRowViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    Column {
        // Exibir a lista de stitch rows
        newStitchRowState.stitchRows.forEach { stitchRow ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(text = "Row ${stitchRow.rowNumber}: ${stitchRow.instructions}, ${stitchRow.stitches} stitches")
            }
        }

        // Instructions
        TextField(
            value = newStitchRowState.instructions,
            singleLine = true,
            onValueChange = { newInstructions -> newStitchRowViewModel.onEvent(NewStitchRowUiEvent.UpdateInstructions(newInstructions)) },
            placeholder = { Text(text = "Chain 7") },
            label = { Text(text = "Row instructions") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Number of stitches in this row
        TextField(
            value = newStitchRowState.stitches.toString(),
            singleLine = true,
            onValueChange = { newValue ->
                val intValue = newValue.toIntOrNull() ?: 0
                newStitchRowViewModel.onEvent(NewStitchRowUiEvent.UpdateStitches(intValue))
            },
            label = { Text(text = "Number of Stitches") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Add Stitch Row Button
        Button(onClick = { newStitchRowViewModel.onEvent(NewStitchRowUiEvent.NewStitchRow) }) {
            Text("Add Stitch Row")
        }
    }
}
