package com.example.pontoalto.model.repository

import com.example.pontoalto.model.dao.StitchRowDao
import com.example.pontoalto.model.entity.Recipe
import com.example.pontoalto.model.entity.StitchRow

class StitchRowRepository(private val stitchRowDao: StitchRowDao) {
    suspend fun insertStitchRow(stitchRow: StitchRow ) {
        return stitchRowDao.insertStitchRow(stitchRow)
    }

    suspend fun getStitchRowByRecipe(inRecipeName: String): List<StitchRow>{
        return stitchRowDao.getStitchRowByRecipe(inRecipeName)
    }

    suspend fun getStitchRowByNumber(rowNumber: Int): StitchRow {
        return stitchRowDao.getStitchRowByNumber(rowNumber)
    }

    suspend fun getMaxRowNumber(recipeName: String): Int {
        return stitchRowDao.getMaxRowNumber(recipeName) ?: 0
    }

}