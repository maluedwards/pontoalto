package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.RecipeDao
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.RecipeWithRows

class RecipeRepository (private val recipeDao: RecipeDao) {
    suspend fun insertRecipe(recipe: Recipe){
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getAllRecipes(): Array<Recipe>{
        return recipeDao.loadAllRecipes()
    }

    fun getRecipeByName(recipeName: String): RecipeWithRows? {
        return recipeDao.getRecipeByName(recipeName)
    }

}