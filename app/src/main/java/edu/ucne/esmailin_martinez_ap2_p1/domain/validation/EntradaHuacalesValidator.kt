package edu.ucne.esmailin_martinez_ap2_p1.domain.validation

object EntradaHuacalesValidator {

    fun validateNombreCliente(nombre: String): ValidationResult {
        return if (nombre.isBlank()) {
            ValidationResult(false, "El nombre del cliente es obligatorio")
        } else {
            ValidationResult(true)
        }
    }

    fun validateCantidad(cantidadStr: String): ValidationResult {
        val cantidad = cantidadStr.toIntOrNull()
        return if (cantidad == null || cantidad <= 0) {
            ValidationResult(false, "La cantidad debe ser mayor a 0")
        } else {
            ValidationResult(true)
        }
    }

    fun validatePrecio(precioStr: String): ValidationResult {
        val precio = precioStr.toDoubleOrNull()
        return if (precio == null || precio < 0) {
            ValidationResult(false, "El precio no puede ser negativo")
        } else {
            ValidationResult(true)
        }
    }

    fun validateFecha(fechaStr: String): ValidationResult {
        if (fechaStr.isBlank()) return ValidationResult(false, "La fecha es obligatoria")
        return try {
            val displayDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("es", "DO"))
            displayDateFormat.isLenient = false
            displayDateFormat.parse(fechaStr)
            ValidationResult(true)
        } catch (e: Exception) {
            ValidationResult(false, "Fecha inválida, use el formato yyyy-MM-dd")
        }
    }
    fun validateDescripcion(descripcion: String): ValidationResult {
        return if (descripcion.isBlank()) {
            ValidationResult(false, "La descripción es obligatoria")
        } else {
            ValidationResult(true)
        }
    }

}
