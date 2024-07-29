package com.example.pontoalto.view.screens

import android.view.Menu
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    newStitchRowViewModel: NewStitchRowViewModel,
    onNewRecipeSuccess: () -> Unit
    ) {

    val newRecipeState = newRecipeViewModel.uiState.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val stitchRowsState by newStitchRowViewModel.stitchRowsState.collectAsState()

    Layout(

        newRecipeState = newRecipeState,
        newRecipeViewModel = newRecipeViewModel,
        onNewRecipeSuccess = { /*TODO*/ },
        onCreateClick = { /*TODO*/ },
        newStitchRowViewModel,
        newStitchRowState = stitchRowsState,
        keyboardController = keyboardController,
        navController = navController
    )
}

@Composable
fun Layout(
    newRecipeState: NewRecipeState,
    newRecipeViewModel: NewRecipeViewModel,
    onNewRecipeSuccess: () -> Unit,
    onCreateClick: () -> Unit,
    newStitchRowViewModel: NewStitchRowViewModel,
    newStitchRowState: List<NewStitchRowState>,
    keyboardController: SoftwareKeyboardController?,
    navController: NavHostController
){ PontoAltoTheme {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MyHeader() },
        bottomBar = { MyNavBar( listRecipes = false, home = false, newRecipe = true, navController) },
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
                    .fillMaxSize(0.98.toFloat())
            ) {
                Text(text = "New Recipe",
                    modifier = Modifier.padding(8.dp)
                )
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)){

                    //recipe name text field
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newRecipeState.recipeName,
                        onValueChange = { newRecipeViewModel.onEvent((NewRecipeUiEvent::UpdateRecipeName)(it)) },
                        placeholder = {
                            Text("Recipe Name")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {keyboardController?.hide() }
                        ),
                    )
                    
                    Spacer(modifier = Modifier.fillMaxWidth())


                    //difficulty menu
                    Box (modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart)
                        .padding(6.dp)
                    ) {
                        var expand by remember { mutableStateOf(false) }

                        //make difficulty appear on button after choosing one
                        OutlinedButton(onClick = { expand = true},
                            modifier = Modifier.padding(6.dp)) {
                            Text(text = "Difficulty")
                        }

                        //add press effect to items
                        DropdownMenu(
                            expanded = expand,
                            onDismissRequest = { expand = false},
                            //modifier = Modifier.fillMaxSize()
                        ) {
                            DropdownMenuItem(
                                text = {  Text(text = "Advanced")},
                                onClick = {
                                    newRecipeViewModel.onEvent((NewRecipeUiEvent::UpdateDifficulty)(3));
                                    expand = false
                                }
                                )
                            DropdownMenuItem(
                                text = {  Text(text = "Intermediate")},
                                onClick = {
                                    newRecipeViewModel.onEvent((NewRecipeUiEvent::UpdateDifficulty)(2));
                                    expand = false
                                })
                            DropdownMenuItem(
                                text = {  Text(text = "Basic")},
                                onClick = {
                                    newRecipeViewModel.onEvent((NewRecipeUiEvent::UpdateDifficulty)(1));
                                    expand = false
                                })
                        }
                    }


                    
                    Spacer(modifier = Modifier.fillMaxWidth())
                    HorizontalDivider()
                    //stitch rows beginning
                    val stitchRowsState by newStitchRowViewModel.stitchRowsState.collectAsState()

                    Column {
                        Button(onClick = { newStitchRowViewModel.addNewRow() }) {
                            Text(text = "Add Stitch Row")
                        }
                        stitchRowsState.forEachIndexed { index, newStitchRowState ->
                            NewRow(
                                newStitchRowState = newStitchRowState,
                                newStitchRowViewModel = newStitchRowViewModel,
                                keyboardController = keyboardController,
                                index = index
                            )
                            Spacer(modifier = Modifier.height(8.dp))
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
    keyboardController: SoftwareKeyboardController?,
    index: Int
) {
    Column {
        // Instructions
        TextField(
            value = newStitchRowState.instructions,
            singleLine = true,
            onValueChange = { newInstructions ->
                newStitchRowViewModel.onEvent(NewStitchRowUiEvent.UpdateInstructions(newInstructions), index)
            },
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
        Spacer(modifier = Modifier.fillMaxWidth())
        // Number of stitches
        var inputValue by remember { mutableStateOf(newStitchRowState.stitches.toString()) }
        TextField(
            value = inputValue,
            singleLine = true,
            onValueChange = { newValue ->
                inputValue = newValue
                val intValue = newValue.toIntOrNull() ?: 0
                newStitchRowViewModel.onEvent(NewStitchRowUiEvent.UpdateStitches(intValue), index)
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
        Spacer(modifier = Modifier.height(8.dp))
        // Save button for each row
        Button(onClick = { newStitchRowViewModel.onEvent(NewStitchRowUiEvent.SaveStitchRow(index), index) }) {
            Text(text = "Save Stitch Row")
        }
    }
}