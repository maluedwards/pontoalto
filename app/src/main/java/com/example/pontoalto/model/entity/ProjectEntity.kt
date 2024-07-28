package com.example.pontoalto.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey
    val projectName: String,
    val recipeUsed: String
)