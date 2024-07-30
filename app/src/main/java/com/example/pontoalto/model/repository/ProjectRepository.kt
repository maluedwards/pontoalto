package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.ProjectDao
import com.example.pontoalto.model.entity.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun insertProject(project: Project) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: Project) {
        projectDao.updateProject(project)
    }
}
