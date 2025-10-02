package edu.ucne.esmailin_martinez_ap2_p1.domain.model

data class EntradaHuacales(
    val identradaHuacales: Int = 0,
    val fecha: String = "",
    val descripcion: String = "",
    val nombreCliente: String = "",
    val cantidad: Int = 0,
    val precio: Double = 0.0
)
