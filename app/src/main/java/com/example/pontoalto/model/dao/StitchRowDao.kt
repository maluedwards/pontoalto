package com.example.pontoalto.model.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pontoalto.model.entity.StitchRow
import com.example.pontoalto.model.repository.StitchRowRepository
import kotlinx.coroutines.flow.Flow

@Dao
interface StitchRowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStitchRow(stitchRow: StitchRow)

    @Delete
    suspend fun deleteStitchRow(vararg stitchRow: StitchRow)

    @Query("SELECT * FROM StitchRow WHERE inRecipeName = :recipeName ORDER BY rowNumber")
    fun getStitchRowByRecipe(recipeName: String ): Flow<List<StitchRow>>

    @Query("SELECT * FROM StitchRow WHERE rowNumber = :rowNumber")
    fun getStitchRowByNumber(rowNumber: Int ): StitchRow

    @Query("SELECT MAX(rowNumber) FROM StitchRow WHERE inRecipeName = :recipeName")
    suspend fun getMaxRowNumber(recipeName: String): Int?
}