@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.ui.theme.PontoAltoTheme

@Composable
fun RecipesScreen(navController: NavHostController) {
    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = true, home = false, projects = false, navController) },
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
                    Text(text = "Recipes",
                        modifier = Modifier.padding(8.dp)
                    )
                    Column (Modifier
                        .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)){
                        ReceitaVer()
                        ReceitaVer()
                    }

                }
            }
        }
    }
}

