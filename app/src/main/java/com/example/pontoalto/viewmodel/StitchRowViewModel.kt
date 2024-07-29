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

    //
    private val _stitchRowsState = MutableStateFlow<List<NewStitchRowState>>(emptyList())
    val stitchRowsState: StateFlow<List<NewStitchRowState>> = _stitchRowsState.asStateFlow()

    fun addNewRow() {
        val newRowState = NewStitchRowState() // Cria um novo estado padrão
        _stitchRowsState.update { it + newRowState }
    }


    fun onEvent(event: NewStitchRowUiEvent, index: Int) {
        when (event) {
            is NewStitchRowUiEvent.UpdateInstructions -> {
                _stitchRowsState.update {
                    it.toMutableList().apply {
                        this[index] = this[index].copy(instructions = event.instructions)
                    }
                }
            }
            is NewStitchRowUiEvent.UpdateStitches -> {
                _stitchRowsState.update {
                    it.toMutableList().apply {
                        this[index] = this[index].copy(stitches = event.stitches)
                    }
                }
            }
            is NewStitchRowUiEvent.NewStitchRow -> { addNewRow() }
            is NewStitchRowUiEvent.SaveStitchRow -> { saveStitchRow(index) }
        }
    }

    private fun saveStitchRow(index: Int) {
        viewModelScope.launch {
            val state = _stitchRowsState.value[index]
            if (state.instructions.isBlank() || state.stitches == 0) {
                _stitchRowsState.update {
                    it.toMutableList().apply {
                        this[index] = this[index].copy(error = "All fields are required")
                    }
                }
                return@launch
            }

            val stitchRow = StitchRow(
                rowNumber = state.rowNumber,
                inRecipeName = state.inRecipeName,
                instructions = state.instructions,
                stitches = state.stitches
            )
            stitchRowRepository.insertStitchRow(stitchRow)
            _stitchRowsState.update {
                it.toMutableList().apply {
                    this[index] = this[index].copy(isRegistered = true, isLoading = false)
                }
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
