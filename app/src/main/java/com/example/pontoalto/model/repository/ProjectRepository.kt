package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.ProjectDao
import com.example.pontoalto.model.entity.Project

class ProjectRepository (private val projectDao: ProjectDao) {
    suspend fun insertProject(project: Project){
        projectDao.insertProject(project)
    }

}