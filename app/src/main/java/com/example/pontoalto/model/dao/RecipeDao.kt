package com.example.pontoalto.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.RecipeWithRows
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(vararg recipe: Recipe)

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM Recipe WHERE recipeName = :recipeName")
    fun getRecipeByName(recipeName: String): RecipeWithRows?


}