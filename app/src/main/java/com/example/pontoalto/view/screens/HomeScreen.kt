@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto.view.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
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
import com.example.pontoalto.model.entity.Project
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.ProjectViewModel

@Composable
fun HomeScreen(navController: NavHostController, projectViewModel: ProjectViewModel) {

    val projects by projectViewModel.projects.collectAsState()

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
            .background(gradient)) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { MyHeader() },
                bottomBar = { MyNavBar(listRecipes = false, home = true, newRecipe = false, navController = navController) },
                containerColor = Color.Transparent
            ) { innerPadding ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Projetos atuais",
                        modifier = Modifier.padding(30.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = customFont,
                            fontSize = 24.sp,
                            color = Color(0xFFFF84CE)
                        )
                    )
                    Column(Modifier.padding(20.dp)) {
                        projects.forEach { project ->
                            ProjectItem(project = project, navController = navController)
                        }
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



