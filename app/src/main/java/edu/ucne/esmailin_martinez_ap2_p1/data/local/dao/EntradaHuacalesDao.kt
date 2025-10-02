package edu.ucne.esmailin_martinez_ap2_p1.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntradaHuacalesDao {

    @Query("SELECT * FROM EntradaHuacales ORDER BY identradaHuacales DESC")
    fun observeAll(): Flow<List<EntradaHuacalesEntity>>

    @Query("SELECT * FROM EntradaHuacales WHERE identradaHuacales = :id")
    suspend fun getById(id: Int): EntradaHuacalesEntity?

    @Upsert
    suspend fun upsert(entrada: EntradaHuacalesEntity)

    @Delete
    suspend fun delete(entrada: EntradaHuacalesEntity)

    @Query("DELETE FROM EntradaHuacales WHERE identradaHuacales = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM EntradaHuacales WHERE nombreCliente COLLATE NOCASE = :name COLLATE NOCASE LIMIT 1")
    suspend fun getByClienteName(name: String): EntradaHuacalesEntity?
}
