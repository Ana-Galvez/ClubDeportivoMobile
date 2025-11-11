package com.example.clubdeportivomovile

data class Cuotas(
    val id: Int,
    val monto: Double,
    val modoPago: String,
    val estado: String,
    val fechaPago: String,
    val fechaVencto: String,
    val cantCuotas: Int,
    val ultDigitosTarj: Int
)
