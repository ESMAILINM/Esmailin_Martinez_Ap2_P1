package edu.ucne.esmailin_martinez_ap2_p1.domain.usecase

import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import javax.inject.Inject

class GetEntradaHuacalesUseCase @Inject constructor(
    private val repository: EntradaHuacalesRepository
) {
    suspend operator fun invoke(id: Int): EntradaHuacales? {
        val entity = repository.getEntrada(id) ?: return null
        return EntradaHuacales(
            identradaHuacales = entity.identradaHuacales,
            fecha = entity.fecha,
            descripcion = entity.descripcion,
            nombreCliente = entity.nombreCliente,
            cantidad = entity.cantidad,
            precio = entity.precio
        )
    }
}
