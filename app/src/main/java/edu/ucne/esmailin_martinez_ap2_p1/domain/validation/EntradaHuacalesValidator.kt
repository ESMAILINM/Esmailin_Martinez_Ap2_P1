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
}
