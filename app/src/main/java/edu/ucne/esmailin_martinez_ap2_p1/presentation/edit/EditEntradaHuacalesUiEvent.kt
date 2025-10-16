package edu.ucne.esmailin_martinez_ap2_p1.presentation.edit

sealed interface EditEntradaHuacalesUiEvent {
    data class Load(val id: Int?) : EditEntradaHuacalesUiEvent
    data class NombreClienteChanged(val nombreCliente: String) : EditEntradaHuacalesUiEvent
    data class DescripcionChanged(val descripcion: String) : EditEntradaHuacalesUiEvent
    data class CantidadChanged(val cantidad: String) : EditEntradaHuacalesUiEvent
    data class PrecioChanged(val precio: String) : EditEntradaHuacalesUiEvent
    data class FechaChanged(val value: String) : EditEntradaHuacalesUiEvent
    data class ImporteChanged(val value: String) : EditEntradaHuacalesUiEvent

    object Save : EditEntradaHuacalesUiEvent
    object Delete : EditEntradaHuacalesUiEvent
}
