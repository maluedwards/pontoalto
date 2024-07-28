package com.example.pontoalto.viewmodel.state

import com.example.pontoalto.model.entity.StitchRow

data class NewRecipeState(
    val recipeName : String = "",
    val totalStitches : Int = 0,
    val difficulty : Int = 1,
    val rows : Int = 0,
    val isLoading : Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
) {
    fun clearError() = copy(error = null)
}
