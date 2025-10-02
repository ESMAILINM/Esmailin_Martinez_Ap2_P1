package edu.ucne.esmailin_martinez_ap2_p1.data.repository

import edu.ucne.esmailin_martinez_ap2_p1.data.local.dao.EntradaHuacalesDao
import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EntradaHuacalesRepositoryImpl @Inject constructor(
    private val dao: EntradaHuacalesDao
) : EntradaHuacalesRepository {

    override fun observeEntradas(): Flow<List<EntradaHuacalesEntity>> =
        dao.observeAll()

    override suspend fun getEntrada(id: Int): EntradaHuacalesEntity? =
        dao.getById(id)

    override suspend fun upsertEntrada(entrada: EntradaHuacalesEntity): Int {
        dao.upsert(entrada)
        return entrada.identradaHuacales
    }

    override suspend fun deleteEntrada(id: Int) =
        dao.delete(id)

    override suspend fun getEntradaByCliente(name: String): EntradaHuacalesEntity? =
        dao.getByClienteName(name)
}
