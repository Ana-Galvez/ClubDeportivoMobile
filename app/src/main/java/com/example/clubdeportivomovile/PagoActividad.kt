package com.example.clubdeportivomovile
// Al tener más de 2 datos utilizo Data Class
data class PagoActividad(
    val id: Int,
    val idCliente: Int,
    val idActividad: Int,
    val fechaPago: String,
    val modoPago: String,
    val monto: Double,
    val estado: String,
)
