package edu.ucne.esmailin_martinez_ap2_p1.domain.usecase

import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveEntradaHuacalesUseCase @Inject constructor(
    private val repository: EntradaHuacalesRepository
) {
    operator fun invoke(): Flow<List<EntradaHuacales>> {
        return repository.observeEntradas().map { list ->
            list.map { entity ->
                EntradaHuacales(
                    identradaHuacales = entity.identradaHuacales,
                    fecha = entity.fecha,
                    descripcion = entity.descripcion,
                    nombreCliente = entity.nombreCliente,
                    cantidad = entity.cantidad,
                    precio = entity.precio
                )
            }
        }
    }
}
