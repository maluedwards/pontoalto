package com.example.pontoalto.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pontoalto.model.entity.Project
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM projects WHERE projectName = :name LIMIT 1")
    suspend fun getProjectByName(name: String): Project

    @Query("UPDATE projects SET currentStitch = :newCount WHERE projectName = :projectName")
    suspend fun updateCurrentStitch(projectName: String, newCount: Int)

}

