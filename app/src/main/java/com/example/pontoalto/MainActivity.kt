@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pontoalto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.pontoalto.view.screens.NewRecipeScreen
import com.example.pontoalto.view.screens.RecipeDetailsScreen
import com.example.pontoalto.view.screens.RecipesScreen
import com.example.pontoalto.viewmodel.NewRecipeViewModel
import com.example.pontoalto.viewmodel.NewRecipeViewModelFactory
import com.example.pontoalto.viewmodel.NewStitchRowViewModel
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
    }
}

@Composable
fun MyHeader() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = Color.White,
        ),
        title = {
            Text("Ponto Alto")
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
            icon = { Icon( Icons.Filled.Menu, contentDescription = "List" ) }
        )
        NavigationBarItem(
            selected = home,
            onClick = { navController.navigate("home")  },
            icon = { Icon( Icons.Filled.Home, contentDescription = "Home" ) }
        )
        NavigationBarItem(
            selected = newRecipe,
            onClick = { navController.navigate("new-recipe") },
            icon = { Icon( Icons.Filled.Create, contentDescription = "New Recipe" ) }
        )
    }
}

