package com.example.pontoalto.viewmodel.state

data class NewRecipeState(
    val recipeName : String = "",
    val difficulty : Int = 1,
    val isLoading : Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
) {
    fun clearError() = copy(error = null)
}
