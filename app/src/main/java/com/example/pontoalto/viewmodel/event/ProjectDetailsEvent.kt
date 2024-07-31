package com.example.pontoalto.viewmodel.event

sealed class ProjectDetailsUiEvent {
    data class IncrementStitch(val amount: Int) : ProjectDetailsUiEvent()
    data class DecrementStitch(val amount: Int) : ProjectDetailsUiEvent()
    data object LoadProject : ProjectDetailsUiEvent()
}