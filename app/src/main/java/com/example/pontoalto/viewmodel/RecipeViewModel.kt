package com.example.pontoalto.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow
import com.example.pontoalto.model.repository.RecipeRepository
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.state.NewRecipeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class NewRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    // Expose UI state
    private val _uiState = MutableStateFlow(NewRecipeState())
    val uiState: StateFlow<NewRecipeState> = _uiState.asStateFlow()

    fun onEvent(event: NewRecipeUiEvent) {
        when (event) {
            is NewRecipeUiEvent.UpdateRecipeName -> {
                _uiState.update { it.copy(recipeName = event.recipeName) }
            }
            is NewRecipeUiEvent.UpdateDifficulty -> {
                _uiState.update { it.copy(difficulty = event.difficulty) }
            }
            is NewRecipeUiEvent.SaveRecipe -> {
                saveRecipe()
            }
        }
    }

    private fun saveRecipe() {
        val state = _uiState.value
        if (state.recipeName.isBlank() || state.difficulty == 0) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Save recipe
                val recipe = Recipe(
                    recipeName = state.recipeName,
                    difficulty = state.difficulty
                )
                recipeRepository.insertRecipe(recipe)

                _uiState.update { it.copy(isRegistered = true, isLoading = false) }
            } catch (e: Exception) {
                Log.e("SaveRecipe", "Error saving recipe", e)
                _uiState.update { it.copy(error = e.message ?: "An error occurred", isLoading = false) }
            }
        }
    }
}

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    init {
        // Launch coroutine to observe changes from the repository and update _recipes
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipeList ->
                _recipes.value = recipeList
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