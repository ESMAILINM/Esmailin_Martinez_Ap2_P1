package edu.ucne.esmailin_martinez_ap2_p1.domain.usecase

import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity
import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import javax.inject.Inject

class UpsertEntradaHuacalesUseCase @Inject constructor(
    private val repository: EntradaHuacalesRepository
) {
    suspend operator fun invoke(entrada: EntradaHuacales): Result<Int> {

        if (entrada.nombreCliente.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del cliente es obligatorio"))
        }
        if (entrada.cantidad <= 0) {
            return Result.failure(IllegalArgumentException("La cantidad debe ser mayor a 0"))
        }
        if (entrada.precio < 0) {
            return Result.failure(IllegalArgumentException("El precio no puede ser negativo"))
        }

        val entity = EntradaHuacalesEntity(
            identradaHuacales = entrada.identradaHuacales,
            fecha = entrada.fecha,
            descripcion = entrada.descripcion,
            nombreCliente = entrada.nombreCliente,
            cantidad = entrada.cantidad,
            precio = entrada.precio
        )

        return runCatching { repository.upsertEntrada(entity) }
    }
}
