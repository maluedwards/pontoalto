package com.example.pontoalto.viewmodel.event

sealed class NewStitchRowUiEvent {
    data class UpdateInstructions(val instructions: String) : NewStitchRowUiEvent()
    data class UpdateStitches(val stitches: Int) : NewStitchRowUiEvent()
    data object NewStitchRow : NewStitchRowUiEvent()
}
