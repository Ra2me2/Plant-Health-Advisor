package com.example.planter_app.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(data: PlantTable)

    @Update
    suspend fun updateData(data: PlantTable)

    @Delete
    suspend fun deleteData(data: PlantTable)

    @Query("DELETE FROM data_table")
    suspend fun deleteAllData()


    @Query("SELECT * FROM data_table")
    fun readAllData(): Flow<List<PlantTable>>
}