package edu.ucne.esmailin_martinez_ap2_p1.data.local.mapper

import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity
import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales

fun EntradaHuacalesEntity.toDomain(): EntradaHuacales = EntradaHuacales(
    identradaHuacales = identradaHuacales,
    fecha = fecha,
    descripcion = descripcion,
    nombreCliente = nombreCliente,
    cantidad = cantidad,
    precio = precio
)

fun EntradaHuacales.toEntity(): EntradaHuacalesEntity = EntradaHuacalesEntity(
    identradaHuacales = identradaHuacales,
    fecha = fecha,
    descripcion = descripcion,
    nombreCliente = nombreCliente,
    cantidad = cantidad,
    precio = precio
)
