@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import com.example.pontoalto.R

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
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(projects) { project ->
                        ProjectCard(project = project, onClick = {
                            val projectName = project.projectName
                            navController.navigate("project-details/$projectName")
                        })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = project.projectName, style = MaterialTheme.typography.titleMedium)
            Text(text = "Recipe: ${project.recipeName}", style = MaterialTheme.typography.bodyMedium)
        }

    }
}



