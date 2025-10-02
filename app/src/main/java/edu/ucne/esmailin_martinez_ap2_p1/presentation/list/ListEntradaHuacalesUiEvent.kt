package edu.ucne.esmailin_martinez_ap2_p1.presentation.list

import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales

interface ListEntradaHuacalesUiEvent {
    data class OnDeleteEntradaClick(val entrada: EntradaHuacales) : ListEntradaHuacalesUiEvent
}
