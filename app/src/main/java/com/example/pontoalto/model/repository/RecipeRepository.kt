package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.RecipeDao
import com.example.pontoalto.model.entity.Recipe

class RecipeRepository (private val recipeDao: RecipeDao) {
    suspend fun insertRecipe(recipe: Recipe){
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getRecipeByName(recipeName: String): Recipe? {
        return recipeDao.getRecipeByName(recipeName)
    }

}