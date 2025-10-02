package edu.ucne.esmailin_martinez_ap2_p1.domain.repository

import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity
import kotlinx.coroutines.flow.Flow

interface EntradaHuacalesRepository {
    fun observeEntradas(): Flow<List<EntradaHuacalesEntity>>

    suspend fun getEntrada(id: Int): EntradaHuacalesEntity?

    suspend fun upsertEntrada(entrada: EntradaHuacalesEntity): Int
    suspend fun deleteEntrada(id: Int)

    suspend fun getEntradaByCliente(name: String): EntradaHuacalesEntity?
}
