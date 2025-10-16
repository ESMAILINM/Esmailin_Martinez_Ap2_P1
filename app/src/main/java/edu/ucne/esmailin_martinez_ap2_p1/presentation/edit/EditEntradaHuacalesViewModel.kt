package edu.ucne.esmailin_martinez_ap2_p1.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.domain.usecase.DeleteEntradaHuacalesUseCase
import edu.ucne.esmailin_martinez_ap2_p1.domain.usecase.GetEntradaHuacalesUseCase
import edu.ucne.esmailin_martinez_ap2_p1.domain.usecase.UpsertEntradaHuacalesUseCase
import edu.ucne.esmailin_martinez_ap2_p1.domain.validation.EntradaHuacalesValidator
import edu.ucne.esmailin_martinez_ap2_p1.domain.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditEntradaHuacalesViewModel @Inject constructor(
    private val upsertEntradaHuacalesUseCase: UpsertEntradaHuacalesUseCase,
    private val getEntradaHuacalesUseCase: GetEntradaHuacalesUseCase,
    private val deleteEntradaHuacalesUseCase: DeleteEntradaHuacalesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditEntradaHuacalesUiState())
    val state: StateFlow<EditEntradaHuacalesUiState> = _state.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "DO"))

    fun onEvent(event: EditEntradaHuacalesUiEvent) {
        when (event) {
            is EditEntradaHuacalesUiEvent.Load -> onLoad(event.id)
            is EditEntradaHuacalesUiEvent.NombreClienteChanged -> _state.update {
                it.copy(nombreCliente = event.nombreCliente, nombreClienteError = null)
            }
            is EditEntradaHuacalesUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.descripcion, descripcionError = null)
            }
            is EditEntradaHuacalesUiEvent.CantidadChanged -> _state.update {
                val cantidad = event.cantidad
                val precio = it.precio
                val importe = calcularImporte(cantidad, precio)
                it.copy(cantidad = cantidad, cantidadError = null, importe = importe)
            }
            is EditEntradaHuacalesUiEvent.PrecioChanged -> _state.update {
                val precio = event.precio
                val cantidad = it.cantidad
                val importe = calcularImporte(cantidad, precio)
                it.copy(precio = precio, precioError = null, importe = importe)
            }
            is EditEntradaHuacalesUiEvent.FechaChanged -> _state.update {
                it.copy(fecha = event.value, fechaError = null)
            }
            EditEntradaHuacalesUiEvent.Save -> onSave()
            EditEntradaHuacalesUiEvent.Delete -> onDelete()
            else -> {}
        }
    }

    private fun calcularImporte(cantidadStr: String, precioStr: String): Double {
        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val precio = precioStr.toDoubleOrNull() ?: 0.0
        return cantidad * precio
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update {
                it.copy(
                    isLoading = false,
                    entradaId = null,
                    nombreCliente = "",
                    descripcion = "",
                    cantidad = "",
                    precio = "",
                    fecha = "",
                    importe = 0.0,
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false
                )
            }
            return
        }
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val entrada = getEntradaHuacalesUseCase(id)
                _state.update {
                    if (entrada != null) {
                        it.copy(
                            entradaId = entrada.identradaHuacales,
                            nombreCliente = entrada.nombreCliente,
                            descripcion = entrada.descripcion,
                            cantidad = entrada.cantidad.toString(),
                            precio = entrada.precio.toString(),
                            fecha = entrada.fecha,
                            importe = entrada.cantidad * entrada.precio,
                            isLoading = false,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Entrada con ID $id no encontrada.",
                            entradaId = null,
                            nombreCliente = "",
                            descripcion = "",
                            cantidad = "",
                            precio = "",
                            fecha = "",
                            importe = 0.0,
                            canBeDeleted = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido al cargar la entrada.",
                        canBeDeleted = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombreCliente = state.value.nombreCliente
        val descripcion = state.value.descripcion
        val cantidadStr = state.value.cantidad
        val precioStr = state.value.precio
        val fechaStr = state.value.fecha
        val currentEntradaId = state.value.entradaId

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSaving = true,
                    nombreClienteError = null,
                    descripcionError = null,
                    cantidadError = null,
                    precioError = null,
                    fechaError = null,
                    errorMessage = null
                )
            }

            val nombreValidation: ValidationResult = EntradaHuacalesValidator.validateNombreCliente(nombreCliente)
            val cantidadValidation: ValidationResult = EntradaHuacalesValidator.validateCantidad(cantidadStr)
            val precioValidation: ValidationResult = EntradaHuacalesValidator.validatePrecio(precioStr)
            val fechaValidation: ValidationResult = EntradaHuacalesValidator.validateFecha(fechaStr)
            val descripcionValidation = EntradaHuacalesValidator.validateDescripcion(descripcion)

            val nombreClienteError = if (!nombreValidation.isValid) nombreValidation.errorMessage else null
            val cantidadError = if (!cantidadValidation.isValid) cantidadValidation.errorMessage else null
            val precioError = if (!precioValidation.isValid) precioValidation.errorMessage else null
            val fechaError = if (!fechaValidation.isValid) fechaValidation.errorMessage else null
            val descripcionError = if (!descripcionValidation.isValid) descripcionValidation.errorMessage else null

            val hayErrores = listOf(nombreClienteError, cantidadError, precioError, fechaError).any { it != null }

            if (hayErrores) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreClienteError = nombreClienteError,
                        cantidadError = cantidadError,
                        precioError = precioError,
                        fechaError = fechaError,
                        descripcionError = descripcionError,
                        errorMessage = "Por favor, corrija los errores en el formulario."
                    )
                }
                return@launch
            }

            val entrada = EntradaHuacales(
                identradaHuacales = currentEntradaId ?: 0,
                nombreCliente = nombreCliente,
                descripcion = descripcion,
                cantidad = cantidadStr.toInt(),
                precio = precioStr.toDouble(),
                fecha = fechaStr
            )

            val result = upsertEntradaHuacalesUseCase(entrada)
            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        entradaId = newId,
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error desconocido al guardar la entrada."
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val entradaId = _state.value.entradaId
        if (entradaId == null || entradaId == 0) {
            _state.update { it.copy(errorMessage = "No se puede eliminar una entrada sin ID válido.") }
            return
        }
        _state.update { it.copy(isDeleting = true, isSaved = false, errorMessage = null) }
        viewModelScope.launch {
            try {
                deleteEntradaHuacalesUseCase(entradaId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        entradaId = null,
                        nombreCliente = "",
                        descripcion = "",
                        cantidad = "",
                        precio = "",
                        fecha = "",
                        importe = 0.0,
                        canBeDeleted = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar la entrada."
                    )
                }
            }
        }
    }
}
