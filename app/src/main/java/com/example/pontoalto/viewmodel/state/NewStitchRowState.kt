package com.example.pontoalto.viewmodel.state

data class NewStitchRowState(
    val rowNumber: Int = -1,
    val inRecipeName: String = "",
    val instructions: String = "",
    val stitches: Int = 0,
    val isLoading : Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
) {
    fun clearError() = copy(error = null)
}