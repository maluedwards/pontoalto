package com.example.pontoalto.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pontoalto.model.entity.StitchRow
import com.example.pontoalto.model.repository.StitchRowRepository

@Dao
interface StitchRowDao {

    @Insert
    suspend fun insertStitchRow(vararg stitchRow: StitchRow)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stitchRow: StitchRow)

    @Delete
    suspend fun deleteStitchRow(vararg stitchRow: StitchRow)

    @Query("SELECT * FROM StitchRow WHERE inRecipeName = :recipeName")
    fun getStitchRowByRecipe(recipeName: String ): List<StitchRow>

    @Query("SELECT * FROM StitchRow WHERE rowNumber = :rowNumber")
    fun getStitchRowByNumber(rowNumber: Int ): StitchRow

    @Query("SELECT MAX(rowNumber) FROM StitchRow WHERE inRecipeName = :recipeName")
    suspend fun getMaxRowNumber(recipeName: String): Int?
}