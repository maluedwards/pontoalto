package com.example.pontoalto.model.entity

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pontoalto.model.entity.StitchRow

data class StitchRow(
    val instructions: String?,
    val stitches: Int
)

@Entity
data class Recipe(
    @PrimaryKey
    val recipeName: String,
    val totalStitches: Int,
    val difficulty: Int,
    val rows: Int
)