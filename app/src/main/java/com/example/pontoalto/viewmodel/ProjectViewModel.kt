package com.example.pontoalto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pontoalto.model.entity.Project
import com.example.pontoalto.model.repository.ProjectRepository
import com.example.pontoalto.viewmodel.event.NewProjectUiEvent
import com.example.pontoalto.viewmodel.event.ProjectDetailsUiEvent
import com.example.pontoalto.viewmodel.state.NewProjectState
import com.example.pontoalto.viewmodel.state.ProjectDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NewProjectState())
    val uiState: StateFlow<NewProjectState> = _uiState.asStateFlow()

    fun onEvent(event: NewProjectUiEvent) {
        when (event) {
            is NewProjectUiEvent.UpdateProjectName -> {
                _uiState.update { it.copy(projectName = event.projectName) }
            }
            is NewProjectUiEvent.UpdateCurrentStitch -> {
                _uiState.update { it.copy(currentStitch = event.currentStitch) }
            }
            is NewProjectUiEvent.UpdateRecipeName -> {
                _uiState.update { it.copy(recipeName = event.recipeName) }
            }
            is NewProjectUiEvent.SaveProject -> {
                saveProject()
            }
        }
    }

    private fun saveProject() {
        val state = _uiState.value
        if (state.projectName.isBlank()) {
            _uiState.update { it.copy(error = "Project name is required") }
            return
        }

        viewModelScope.launch {
            try {
                val newProject = Project(
                    projectName = state.projectName,
                    recipeName = state.recipeName,
                    currentStitch = state.currentStitch
                )
                projectRepository.insertProject(newProject)
                _uiState.update { NewProjectState() }  // Reset the state after saving
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "An error occurred") }
            }
        }
    }
}

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllProjects().collect { projectList ->
                _projects.value = projectList
            }
        }
    }
}

class ProjectDetailsViewModel(
    private val projectName: String,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailsState())
    val uiState: StateFlow<ProjectDetailsState> = _uiState

    init {
        onEvent(ProjectDetailsUiEvent.LoadProject)
    }

    fun onEvent(event: ProjectDetailsUiEvent) {
        when (event) {
            is ProjectDetailsUiEvent.IncrementStitch -> {
                updateStitchCount(_uiState.value.currentStitch + event.amount)
            }
            is ProjectDetailsUiEvent.DecrementStitch -> {
                updateStitchCount(_uiState.value.currentStitch - event.amount)
            }
            is ProjectDetailsUiEvent.LoadProject -> {
                loadProjectDetails()
            }
        }
    }

    private fun updateStitchCount(newCount: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(currentStitch = newCount)
            projectRepository.updateCurrentStitch(projectName, newCount)
        }
    }

    private fun loadProjectDetails() {
        viewModelScope.launch {
            val project = projectRepository.getProjectByName(projectName)
            _uiState.value = ProjectDetailsState(
                projectName = project.projectName,
                recipeName = project.recipeName,
                currentStitch = project.currentStitch
            )
        }
    }
}

class ProjectViewModelFactory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            return ProjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class NewProjectViewModelFactory(
    private val projectRepository: ProjectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewProjectViewModel(projectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProjectDetailsViewModelFactory(
    private val projectName: String,
    private val projectRepository: ProjectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectDetailsViewModel(projectName, projectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

