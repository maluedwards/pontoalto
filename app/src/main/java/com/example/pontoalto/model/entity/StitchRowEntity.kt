package com.example.pontoalto.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["rowNumber", "inRecipeName"])
data class StitchRow(
    val rowNumber: Int,
    val inRecipeName: String,
    val instructions: String,
    val stitches: Int
)
