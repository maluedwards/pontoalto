package com.example.pontoalto.viewmodel.event

import com.example.pontoalto.model.entity.StitchRow

sealed class NewRecipeUiEvent {
    data class UpdateRecipeName(val recipeName: String) : NewRecipeUiEvent()
    data class UpdateDifficulty(val difficulty: Int) : NewRecipeUiEvent()
    data object SaveRecipe : NewRecipeUiEvent()
    data object ClearState : NewRecipeUiEvent()
}
