package edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EntradaHuacales")
data class EntradaHuacalesEntity(
    @PrimaryKey(autoGenerate = true)
    val identradaHuacales: Int = 0,
    val fecha: String = "",
    val descripcion: String = "",
    val nombreCliente: String = "",
    val cantidad: Int = 0,
    val precio: Double = 0.0
)
