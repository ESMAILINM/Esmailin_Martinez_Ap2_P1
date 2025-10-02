package edu.ucne.esmailin_martinez_ap2_p1.presentation.edit

data class EditEntradaHuacalesUiState(
    val entradaId: Int? = null,
    val fecha: String = "",
    val descripcion: String = "",
    val nombreCliente: String = "",
    val cantidad: String = "",
    val precio: String = "",
    val fechaError: String? = null,
    val descripcionError: String? = null,
    val nombreClienteError: String? = null,
    val cantidadError: String? = null,
    val precioError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false
)
