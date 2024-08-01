package com.example.pontoalto.viewmodel.event

sealed class NewProjectUiEvent {
    data class UpdateProjectName(val projectName: String) : NewProjectUiEvent()
    data class UpdateCurrentStitch(val currentStitch: Int) : NewProjectUiEvent()
    data class UpdateRecipeName(val recipeName: String) : NewProjectUiEvent()
    data object SaveProject : NewProjectUiEvent()
}
