package edu.ucne.esmailin_martinez_ap2_p1.domain.usecase

import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import javax.inject.Inject

class DeleteEntradaHuacalesUseCase @Inject constructor(
    private val repository: EntradaHuacalesRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteEntrada(id)
    }
}

