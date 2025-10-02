package edu.ucne.esmailin_martinez_ap2_p1.presentation.list

import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales

data class ListEntradaHuacalesUiState(
    val entradas: List<EntradaHuacales> = emptyList(),
    val isLoading: Boolean = true
)
