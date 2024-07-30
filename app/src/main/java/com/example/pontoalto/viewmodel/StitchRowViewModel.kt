package com.example.pontoalto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow
import com.example.pontoalto.model.repository.StitchRowRepository
import com.example.pontoalto.viewmodel.event.NewRecipeUiEvent
import com.example.pontoalto.viewmodel.event.NewStitchRowUiEvent
import com.example.pontoalto.viewmodel.state.NewStitchRowState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewStitchRowViewModel(private val stitchRowRepository: StitchRowRepository) : ViewModel() {

    private val _nstitchrstate = MutableStateFlow(NewStitchRowState())
    val uiState: StateFlow<NewStitchRowState> = _nstitchrstate.asStateFlow()

    fun onEvent(event: NewStitchRowUiEvent) {
        when (event) {
            is NewStitchRowUiEvent.UpdateInstructions -> {
                _nstitchrstate.update { it.clearError().copy(instructions = event.instructions) }
            }
            is NewStitchRowUiEvent.UpdateStitches -> {
                _nstitchrstate.update { it.clearError().copy(stitches = event.stitches) }
            }
            is NewStitchRowUiEvent.NewStitchRow -> {
                addNewStitchRow()
            }
        }
    }

    private fun addNewStitchRow() {
        val state = _nstitchrstate.value
        if (state.instructions.isBlank() || state.stitches == 0) {
            _nstitchrstate.update { it.copy(error = "All fields are required") }
            return
        }

        val newStitchRow = StitchRow(
            rowNumber = state.stitchRows.size + 1,
            inRecipeName = state.inRecipeName,
            instructions = state.instructions,
            stitches = state.stitches
        )

        val updatedStitchRows = state.stitchRows + newStitchRow

        _nstitchrstate.update {
            it.copy(
                stitchRows = updatedStitchRows, // Atualize a lista de stitchRows
                instructions = "", // Limpe o campo de instruções
                stitches = 0 // Limpe o campo de pontos
            )
        }
    }
}

class StitchRowViewModelFactory(private val stitchRowRepository: StitchRowRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewStitchRowViewModel::class.java)) {
            return NewStitchRowViewModel(stitchRowRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
