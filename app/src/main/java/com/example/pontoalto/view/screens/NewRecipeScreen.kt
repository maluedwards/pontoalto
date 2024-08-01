package com.example.pontoalto.view.screens

import android.util.Log
import android.view.Menu
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.room.BuiltInTypeConverters
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.R
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.NewRecipeViewModel
import com.example.pontoalto.viewmodel.NewStitchRowViewModel
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.event.NewStitchRowUiEvent
import com.example.pontoalto.viewmodel.state.NewRecipeState
import com.example.pontoalto.viewmodel.state.NewStitchRowState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        if (newRecipeState.isRegistered) {
            newRecipeViewModel.onEvent(NewRecipeUiEvent.ClearState)
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
            .background(gradient)){
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MyHeader() },
                bottomBar = {
                    MyNavBar(
                        listRecipes = false,
                        home = false,
                        newRecipe = true,
                        navController
                    )
                },
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
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Recipe name text field
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = newRecipeState.recipeName,
                                onValueChange = {
                                    newRecipeViewModel.onEvent(
                                        NewRecipeUiEvent.UpdateRecipeName(
                                            it
                                        )
                                    )
                                },
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

                                OutlinedButton(
                                    onClick = { expand = true },
                                    modifier = Modifier.padding(6.dp).fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)

                                ) {
                                    Text(text = difficulty)
                                }

                                DropdownMenu(
                                    expanded = expand,
                                    onDismissRequest = { expand = false }

                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Advanced") },
                                        onClick = {
                                            newRecipeViewModel.onEvent(
                                                NewRecipeUiEvent.UpdateDifficulty(
                                                    3
                                                )
                                            )
                                            difficulty = "Advanced"
                                            expand = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Intermediate") },
                                        onClick = {
                                            newRecipeViewModel.onEvent(
                                                NewRecipeUiEvent.UpdateDifficulty(
                                                    2
                                                )
                                            )
                                            difficulty = "Intermediate"
                                            expand = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "Basic") },
                                        onClick = {
                                            newRecipeViewModel.onEvent(
                                                NewRecipeUiEvent.UpdateDifficulty(
                                                    1
                                                )
                                            )
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
                                    recipeName = newRecipeState.recipeName,
                                    keyboardController = keyboardController
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            // Save Recipe Button
                            Button(
                                onClick = { newRecipeViewModel.onEvent(NewRecipeUiEvent.SaveRecipe) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF84CE)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Save Recipe")
                            }


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
    recipeName: String,
    keyboardController: SoftwareKeyboardController?
) {
    Column {
        // Display the list of stitch rows
        Log.d("NewRow", "Current recipe name: $recipeName")
        newStitchRowState.stitchRows.forEach { stitchRow ->
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Linha ${stitchRow.rowNumber}",
                    color = Color(0xFF5941A9),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "${stitchRow.instructions}, ${stitchRow.stitches} pontos",
                    color = Color(0xFF989898),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Instructions
        TextField(
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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

        // Add Row Button
        Button(
            onClick = {
                newStitchRowViewModel.onEvent(NewStitchRowUiEvent.NewStitchRow(recipeName))
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF84CE)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Add Row")
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Display error message if any
        newStitchRowState.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

