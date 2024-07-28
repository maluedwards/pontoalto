package com.example.pontoalto.viewmodel.event

sealed class NewRecipeUiEvent{
    data class UpdateRecipeName(val recipeName: String) : NewRecipeUiEvent()
    data class UpdateDifficulty(val difficulty: Int) : NewRecipeUiEvent()
    data object NewRecipe : NewRecipeUiEvent()
}