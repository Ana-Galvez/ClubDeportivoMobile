package com.example.clubdeportivomovile

data class Actividad(
    val id: Int,
    val nombre: String,
    val diaSemana: String,
    val hora: String,
    val monto: Double // Usamos Double por los decimales en tu INSERT
)