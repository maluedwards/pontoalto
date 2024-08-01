package com.example.pontoalto.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.first
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
                addNewStitchRow(event.recipeName)
            }
        }
    }

    private fun addNewStitchRow(recipeName: String) {
        val state = _nstitchrstate.value
        if (state.instructions.isBlank() || state.stitches == 0 || recipeName.isBlank()) {
            _nstitchrstate.update { it.copy(error = "All fields are required") }
            return
        }

        viewModelScope.launch {
            try {
                val newStitchRow = StitchRow(
                    rowNumber = state.stitchRows.size + 1,
                    inRecipeName = recipeName,
                    instructions = state.instructions,
                    stitches = state.stitches
                )

                Log.d("NewStitchRowViewModel", "Saving StitchRow: $newStitchRow")
                stitchRowRepository.insertStitchRow(newStitchRow)

                val updatedStitchRows = state.stitchRows + newStitchRow
                _nstitchrstate.update {
                    it.copy(
                        stitchRows = updatedStitchRows,
                        instructions = "",
                        stitches = 0
                    )
                }
                Log.d("NewStitchRowViewModel", "StitchRow saved successfully")
            } catch (e: Exception) {
                Log.e("NewStitchRowViewModel", "Error saving StitchRow", e)
                _nstitchrstate.update { it.copy(error = e.message ?: "An error occurred") }
            }
        }
    }
}

class StitchRowViewModel(private val stitchRowRepository: StitchRowRepository) : ViewModel() {

    private val _stitchRows = MutableStateFlow<List<StitchRow>>(emptyList())
    val stitchRows: StateFlow<List<StitchRow>> = _stitchRows.asStateFlow()

    fun loadStitchRows(recipeName: String) {
        viewModelScope.launch {
            try {
                Log.d("StitchRowViewModel", "Loading stitch rows for recipe: $recipeName")
                stitchRowRepository.getStitchRowsByRecipe(recipeName).collect { rows ->
                    Log.d("StitchRowViewModel", "Collected stitch rows: $rows")
                    _stitchRows.value = rows
                }
            } catch (e: Exception) {
                Log.e("StitchRowViewModel", "Error loading stitch rows", e)
            }
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

class StitchRowsViewModelFactory(
    private val stitchRowRepository: StitchRowRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StitchRowViewModel::class.java)) {
            return StitchRowViewModel(stitchRowRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}