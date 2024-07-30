package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.RecipeDao
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.RecipeWithRows
import kotlinx.coroutines.flow.Flow

class RecipeRepository (private val recipeDao: RecipeDao) {
    suspend fun insertRecipe(recipe: Recipe) {
        return recipeDao.insertRecipe(recipe)
    }

    // Return a Flow that emits the list of recipes
    fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipeByName(recipeName: String): RecipeWithRows? {
        return recipeDao.getRecipeByName(recipeName)
    }


}