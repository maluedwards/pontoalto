package com.example.pontoalto.model.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Recipe(
    @PrimaryKey
    val recipeName: String,
    val difficulty: Int
)

data class RecipeWithRows(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "recipeName",
        entityColumn = "inRecipeName"
    )
    val rows: List<StitchRow>
)