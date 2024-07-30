@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pontoalto.MyHeader
import com.example.pontoalto.MyNavBar
import com.example.pontoalto.model.entity.Project
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.ProjectViewModel

@Composable
fun HomeScreen(navController: NavHostController, projectViewModel: ProjectViewModel) {

    val projects by projectViewModel.projects.collectAsState()

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar( listRecipes = false, home = true, newRecipe = false, navController) },
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
                Text(text = "Current Projects",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleLarge)
                Column(Modifier.padding(10.dp)) {
                    projects.forEach { project ->
                        ProjectItem(project = project, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navegar para os detalhes do projeto (se necessário)
                // navController.navigate("project_details_screen/${project.id}")
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = project.projectName)
    }
}



