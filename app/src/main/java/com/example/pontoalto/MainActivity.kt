@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.pontoalto.model.database.PontoAltoDatabase
import com.example.pontoalto.model.repository.ProjectRepository
import com.example.pontoalto.model.repository.RecipeRepository
import com.example.pontoalto.model.repository.StitchRowRepository
import com.example.pontoalto.view.screens.HomeScreen
import com.example.pontoalto.view.screens.NewProjectScreen
import com.example.pontoalto.view.screens.NewRecipeScreen
import com.example.pontoalto.view.screens.ProjectDetailsScreen
import com.example.pontoalto.view.screens.RecipeDetailsScreen
import com.example.pontoalto.view.screens.RecipesScreen
import com.example.pontoalto.viewmodel.NewProjectViewModel
import com.example.pontoalto.viewmodel.NewProjectViewModelFactory
import com.example.pontoalto.viewmodel.NewRecipeViewModel
import com.example.pontoalto.viewmodel.NewRecipeViewModelFactory
import com.example.pontoalto.viewmodel.NewStitchRowViewModel
import com.example.pontoalto.viewmodel.ProjectDetailsViewModel
import com.example.pontoalto.viewmodel.ProjectDetailsViewModelFactory
import com.example.pontoalto.viewmodel.ProjectViewModel
import com.example.pontoalto.viewmodel.ProjectViewModelFactory
import com.example.pontoalto.viewmodel.RecipeDetailsViewModel
import com.example.pontoalto.viewmodel.RecipeViewModel
import com.example.pontoalto.viewmodel.RecipeViewModelFactory
import com.example.pontoalto.viewmodel.StitchRowViewModel
import com.example.pontoalto.viewmodel.StitchRowViewModelFactory
import com.example.pontoalto.viewmodel.StitchRowsViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PontoAlto()
        }
    }
}


@Composable
fun PontoAlto() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = PontoAltoDatabase.getDatabase(context)
    val recipeDao = db.recipeDao()
    val stitchRowDao = db.stitchRowDao()
    val projectDao = db.projectDao()
    val recipeRepository = RecipeRepository(recipeDao)
    val stitchRowRepository = StitchRowRepository(stitchRowDao)
    val projectRepository = ProjectRepository(projectDao)

    // ViewModel Factory
    val factory = NewRecipeViewModelFactory(recipeRepository)
    val newRecipeViewModel: NewRecipeViewModel = viewModel(factory = factory)
    val newStitchRowViewModel: NewStitchRowViewModel = viewModel(factory = StitchRowViewModelFactory(stitchRowRepository))
    val projectViewModel: ProjectViewModel = viewModel(factory = ProjectViewModelFactory(projectRepository))
    val newProjectViewModel: NewProjectViewModel = viewModel(factory = NewProjectViewModelFactory(projectRepository))
    val recipeDetailsViewModel: RecipeDetailsViewModel = viewModel(factory = RecipeViewModelFactory(recipeRepository))
    val stitchRowViewModel: StitchRowViewModel = viewModel(factory = StitchRowsViewModelFactory(stitchRowRepository))


    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(
            navController,
            projectViewModel
            ) }
        composable("recipes") { RecipesScreen(
            navController,
            recipeViewModel = RecipeViewModel(recipeRepository)
        ) }
        composable("new-recipe") {
            NewRecipeScreen(
                navController = navController,
                newRecipeViewModel = newRecipeViewModel,
                newStitchRowViewModel = newStitchRowViewModel
            )
        }
        composable(
            "recipe-detail/{recipeName}",
            arguments = listOf(navArgument("recipeName") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
            RecipeDetailsScreen(
                navController = navController,
                recipeName = recipeName,
                recipeViewModel = recipeDetailsViewModel,
                stitchRowViewModel = stitchRowViewModel
            )
        }
        composable("new-project/{recipeName}", ) { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""

            NewProjectScreen(
                navController = navController,
                newProjectViewModel = newProjectViewModel,
                recipeName = recipeName
            )
        }
        composable(
            route = "project-details/{projectName}",
            arguments = listOf(navArgument("projectName") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectName = backStackEntry.arguments?.getString("projectName") ?: ""
            val viewModel: ProjectDetailsViewModel = viewModel(
                factory = ProjectDetailsViewModelFactory(
                    projectName = projectName,
                    projectRepository = projectRepository
                )
            )
            ProjectDetailsScreen(
                navController = navController,
                projectName = projectName,
                projectViewModel = viewModel,
                stitchRowViewModel = stitchRowViewModel
            )
        }
    }
}

@Composable
fun MyHeader() {
    val customFont = FontFamily(
        Font(R.font.poetsen_one, FontWeight.Normal)
    )
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFB685E8),
            titleContentColor = Color.White
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Ponto Alto",
                    style = TextStyle(
                        fontFamily = customFont,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
                Image(
                    painter = painterResource(id = R.drawable.logo_ponto_alto),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    )
}



@Composable
fun MyNavBar(
    listRecipes: Boolean,
    home: Boolean,
    newRecipe: Boolean,
    navController: NavHostController
){
    NavigationBar {
        NavigationBarItem(
            selected = listRecipes,
            onClick = { navController.navigate("recipes") },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.book_icon),
                    contentDescription = "List",
                    modifier = Modifier.size(60.dp),
                )
            }
        )
        NavigationBarItem(
            selected = home,
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home, 
                    contentDescription = "Home",
                    modifier = Modifier.size(60.dp), 
                    tint = Color(0xFF5941A8) 
                )
            }
        )
        NavigationBarItem(
            selected = newRecipe,
            onClick = { navController.navigate("new-recipe") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Create, 
                    contentDescription = "New Recipe",
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF5941A8) 
                )
            }
        )
    }
}

