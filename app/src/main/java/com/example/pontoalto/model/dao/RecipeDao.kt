package com.example.pontoalto.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pontoalto.model.entity.Recipe

@Dao
interface RecipeDao{
    @Insert
    suspend fun insertRecipe(vararg recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(vararg recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    fun loadAllRecipes():Array<Recipe>

    @Query("SELECT * FROM Recipe WHERE recipeName = :recipeName")
    suspend fun getRecipeByName(recipeName: String): Recipe?

}