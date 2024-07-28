package com.example.pontoalto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.repository.RecipeRepository
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.state.NewRecipeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewRecipeViewModel(private val recipeRepository: RecipeRepository): ViewModel(){

    // Expose screen UI state
    private val _nrecipeState = MutableStateFlow(NewRecipeState())
    val uiState: StateFlow<NewRecipeState> = _nrecipeState.asStateFlow()

    // Handle business logic
    fun onEvent(event: NewRecipeUiEvent){
        when (event) {
            is NewRecipeUiEvent.UpdateRecipeName -> {
                _nrecipeState.update { it.clearError().copy(recipeName = event.recipeName) }
            }
            is NewRecipeUiEvent.UpdateDifficulty -> {
                _nrecipeState.update { it.clearError().copy(difficulty = event.difficulty) }
            }
            is NewRecipeUiEvent.NewRecipe -> { newRecipe() }

        }
    }

    private fun newRecipe() {
        val state = _nrecipeState.value
        if ( state.recipeName.isBlank() || state.difficulty == 0) {
            _nrecipeState.update { it.copy(error = "All fields are required") }
            return
        }
        if (state.rows == 0){
            _nrecipeState.update { it.copy(error = "At least one row required") }
        }

        _nrecipeState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val existingRecipe = recipeRepository.getRecipeByName(state.recipeName)
            if (existingRecipe != null) {
                _nrecipeState.update { it.copy(error = "A Recipe with this name already exists", isLoading = false) }
            } else {
                val recipe = Recipe(
                    recipeName = state.recipeName,
                    difficulty = state.difficulty,
                    rows = state.rows,
                    totalStitches = state.totalStitches
                )
                recipeRepository.insertRecipe(recipe)
                _nrecipeState.update { it.copy(isRegistered = true, isLoading = false) }
            }
        }
    }


}

class NewRecipeViewModelFactory(private val recipeRepository: RecipeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewRecipeViewModel::class.java)) {
            return NewRecipeViewModel(recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}