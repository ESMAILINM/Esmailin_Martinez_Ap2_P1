package edu.ucne.esmailin_martinez_ap2_p1.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
