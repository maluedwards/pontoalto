package com.example.pontoalto.model.repository

import android.util.Log
import com.example.pontoalto.model.dao.StitchRowDao
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow
import kotlinx.coroutines.flow.Flow

class StitchRowRepository(private val stitchRowDao: StitchRowDao) {
    suspend fun insertStitchRow(stitchRow: StitchRow ) {
        return stitchRowDao.insertStitchRow(stitchRow)
    }

    fun getStitchRowsByRecipe(recipeName: String): Flow<List<StitchRow>> {
        Log.d("Repository", "Fetching stitch rows for recipe: $recipeName")
        return stitchRowDao.getStitchRowByRecipe(recipeName)
    }

    suspend fun getStitchRowByNumber(rowNumber: Int): StitchRow {
        return stitchRowDao.getStitchRowByNumber(rowNumber)
    }

    suspend fun getMaxRowNumber(recipeName: String): Int {
        return stitchRowDao.getMaxRowNumber(recipeName) ?: 0
    }

}