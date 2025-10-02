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
import javax.inject.Inject

@HiltViewModel
class EditEntradaHuacalesViewModel @Inject constructor(
    private val upsertEntradaHuacalesUseCase: UpsertEntradaHuacalesUseCase,
    private val getEntradaHuacalesUseCase: GetEntradaHuacalesUseCase,
    private val deleteEntradaHuacalesUseCase: DeleteEntradaHuacalesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditEntradaHuacalesUiState())
    val state: StateFlow<EditEntradaHuacalesUiState> = _state.asStateFlow()

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
                it.copy(cantidad = event.cantidad, cantidadError = null)
            }
            is EditEntradaHuacalesUiEvent.PrecioChanged -> _state.update {
                it.copy(precio = event.precio, precioError = null)
            }
            EditEntradaHuacalesUiEvent.Save -> onSave()
            EditEntradaHuacalesUiEvent.Delete -> onDelete()
        }
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
        val currentEntradaId = state.value.entradaId

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSaving = true,
                    nombreClienteError = null,
                    descripcionError = null,
                    cantidadError = null,
                    precioError = null,
                    errorMessage = null
                )
            }

            // Validaciones usando validator
            val nombreValidation: ValidationResult = EntradaHuacalesValidator.validateNombreCliente(nombreCliente)
            val cantidadValidation: ValidationResult = EntradaHuacalesValidator.validateCantidad(cantidadStr)
            val precioValidation: ValidationResult = EntradaHuacalesValidator.validatePrecio(precioStr)

            if (!nombreValidation.isValid || !cantidadValidation.isValid || !precioValidation.isValid) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreClienteError = nombreValidation.errorMessage,
                        cantidadError = cantidadValidation.errorMessage,
                        precioError = precioValidation.errorMessage,
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
                precio = precioStr.toDouble()
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
