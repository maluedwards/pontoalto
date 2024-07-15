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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("recipes") { RecipesScreen(navController) }

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
    projects: Boolean,
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
            selected = projects,
            onClick = { /*TODO*/ },
            icon = { Icon( Icons.Filled.Create, contentDescription = "projects" ) }
        )
    }
}

@Composable
fun ReceitaHor(){
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Image(Icons.Filled.AccountCircle, contentDescription = null)
        Text(text = "Receita")
    }
}

@Composable
fun ReceitaVer(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(Icons.Filled.AccountCircle, contentDescription = null)
        Text(text = "Receita")
    }
}