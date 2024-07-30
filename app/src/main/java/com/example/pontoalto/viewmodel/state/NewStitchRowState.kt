package com.example.pontoalto.viewmodel.state

import com.example.pontoalto.model.entity.StitchRow

data class NewStitchRowState(
    val instructions: String = "",
    val stitches: Int = 0,
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val error: String? = null,
    val rowNumber: Int = 0,
    val inRecipeName: String = "",
    val stitchRows: List<StitchRow> = emptyList() // Inclua esta linha
)
 {
    fun clearError() = copy(error = null)
}