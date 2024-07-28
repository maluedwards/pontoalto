package com.example.pontoalto.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.NewRecipeViewModel
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.state.NewRecipeState

@Composable
fun NewRecipeScreen(
    navController: NavHostController,
    newRecipeViewModel: NewRecipeViewModel,
    onNewRecipeSuccess: () -> Unit
    ) {

    val newRecipeState = newRecipeViewModel.uiState.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current

    Layout(
        newRecipeState = newRecipeState,
        newRecipeViewModel = newRecipeViewModel,
        onNewRecipeSuccess = { /*TODO*/ },
        onCreateClick = { /*TODO*/ },
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
                Column (Modifier
                    .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)){
                    TextField(
                        //registerState.email, KeyboardType.Email, RegisterUiEvent::UpdateEmail
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
                            onDone = {
                                newRecipeViewModel.onEvent(NewRecipeUiEvent.NewRecipe)
                                keyboardController?.hide() }
                        ),
                    )
                }

            }
        }
    }
}

}