package com.example.pontoalto

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity
data class Recipe(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val recipeName: String?,
    val totalStitches: Int,
    val difficulty: Int,
    val rows: List<StitchRow>
)

@Entity
data class StitchRow(
    @PrimaryKey val number: Int,
    val stitches: Int,
    val instructions: String?
)

@Dao
interface RecipeDao{
    @Insert
    fun insertAll(vararg recipes: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)

    @Update
    fun updateRecipe(vararg recipes: Recipe)

    @Query("SELECT * FROM Recipe")
    fun loadAllRecipes():Array<Recipe>

}

@Dao
interface StitchRowDao{
    @Insert
    fun insertStitchRow(vararg stitchrows: StitchRow)

    @Delete
    fun deleteStitchRow(stitchrow: StitchRow)

    @Update
    fun updateStitchRow(vararg stitchrows: StitchRow)


}