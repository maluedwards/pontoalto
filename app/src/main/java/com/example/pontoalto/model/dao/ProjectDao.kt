package com.example.pontoalto.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pontoalto.model.entity.Project
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow

@Dao
interface ProjectDao{
    @Insert
    suspend fun insertProject(vararg project: Project)

    @Delete
    fun deleteProject(vararg project: Project)

    @Update
    fun updateProject(vararg project: Project)

    @Query("SELECT * FROM Project")
    fun loadAllProjects():Array<Project>


}