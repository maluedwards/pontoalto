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

    @Query("DELETE FROM recipe WHERE recipeName = :recipeName")
    suspend fun deleteRecipeByName(recipeName: String)

    @Update
    suspend fun updateRecipe(vararg recipe: Recipe)

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM Recipe WHERE recipeName = :recipeName")
    fun getRecipeByName(recipeName: String): Flow<RecipeWithRows>


}