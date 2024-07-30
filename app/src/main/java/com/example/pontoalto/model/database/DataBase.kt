package com.example.pontoalto.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pontoalto.model.dao.*
import com.example.pontoalto.model.entity.*

@Database(
    entities = [
        Recipe::class,
        StitchRow::class,
        Project::class],
    version = 5,
    exportSchema = false)
abstract class PontoAltoDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun stitchRowDao(): StitchRowDao
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: PontoAltoDatabase? = null

        fun getDatabase(context: Context): PontoAltoDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, PontoAltoDatabase::class.java, "pontoalto_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}