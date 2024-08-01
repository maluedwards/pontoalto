package com.example.pontoalto.viewmodel.state

data class NewProjectState(
    val projectName: String = "",
    val recipeName: String = "",
    val currentStitch: Int = 0,
    val error: String? = null
)