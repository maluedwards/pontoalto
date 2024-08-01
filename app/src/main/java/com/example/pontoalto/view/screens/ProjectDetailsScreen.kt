package com.example.pontoalto.view.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.pontoalto.ui.theme.PontoAltoTheme
import com.example.pontoalto.viewmodel.ProjectDetailsViewModel
import com.example.pontoalto.viewmodel.StitchRowViewModel
import com.example.pontoalto.viewmodel.event.ProjectDetailsUiEvent

@Composable
fun ProjectDetailsScreen(
    navController: NavHostController,
    projectViewModel: ProjectDetailsViewModel,
    projectName: String,
    stitchRowViewModel: StitchRowViewModel
) {
    val projectState by projectViewModel.uiState.collectAsState()
    val stitchRowsState by stitchRowViewModel.stitchRows.collectAsState()

    val customFont = FontFamily(
        Font(R.font.poetsen_one, FontWeight.Normal)
    )

    LaunchedEffect(projectState.recipeName) {
        Log.d("ProjectDetailsScreen", "Loading stitch rows for recipe: ${projectState.recipeName}")
        stitchRowViewModel.loadStitchRows(projectState.recipeName)
    }

    PontoAltoTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyHeader() },
            bottomBar = { MyNavBar(listRecipes = false, home = false, newRecipe = false, navController) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) { innerPadding ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Text(text = "Project Name: ${projectState.projectName}",
                        fontFamily = customFont,
                        fontSize = 24.sp,
                        color = Color(0xFFFF84CE)
                        )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Recipe Name: ${projectState.recipeName}",
                        fontFamily = customFont,
                        fontSize = 18.sp,
                        color = Color(0xFFFF84CE)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))



                    val currentRow = stitchRowsState.indexOfFirst { row ->
                        val rowEnd = row.stitches + stitchRowsState.takeWhile { it.rowNumber < row.rowNumber }
                            .sumOf { it.stitches }
                        projectState.currentStitch <= rowEnd
                    } +1

                    Text(text = "Stitch Rows:",
                        fontSize = 20.sp,
                        color = Color(0xFF5941A9)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn (modifier = Modifier.padding(10.dp)){
                        items(stitchRowsState) { row ->
                            val isRowDone = currentRow==row.rowNumber
                            val textColor = if (isRowDone) Color(0xFFFF84CE) else Color.Black
                            Text(
                                text = "Row ${row.rowNumber}: ${row.instructions} (${row.stitches} stitches)",
                                color = textColor
                            )
                        }
                    }

                    Column (modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ){
                        Text(text = "Current Stitch:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp),
                            fontFamily = customFont,
                            color = Color(0xFF5941A9)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "${projectState.currentStitch}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 30.sp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Button(onClick = { projectViewModel.onEvent(ProjectDetailsUiEvent.DecrementStitch(1)) },
                                colors = ButtonDefaults.buttonColors(Color(0xFFB685E8)),
                                shape = RoundedCornerShape(8.dp)
                                ) {
                                Text(text = "-",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp)
                                    )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = { projectViewModel.onEvent(ProjectDetailsUiEvent.IncrementStitch(1))},
                                colors = ButtonDefaults.buttonColors(Color(0xFFB685E8)) ,
                                shape = RoundedCornerShape(8.dp)

                            ) {
                                Text(text = "+",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 29.sp)
                                    )
                            }
                        }
                        Button(
                            onClick = {
                                projectViewModel.onEvent(ProjectDetailsUiEvent.DeleteProject)
                                // Navigate back to the projects list
                                navController.navigate("home") {
                                    popUpTo("project-detail") { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF84CE)),
                            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "End Project", color = Color.White)
                        }
                    }


                }
            }
        }
    }
}
