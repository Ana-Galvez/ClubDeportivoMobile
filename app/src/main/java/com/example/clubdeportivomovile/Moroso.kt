package com.example.clubdeportivomovile

data class Moroso(
    val id: Int,
    val nombreCompleto: String,
    val dni: Int,
    val telefono: String,
    val totalAdeudado: Double
) {
    val totalAdeudadoUI: String
        get() = try {
            val loc = java.util.Locale("es", "AR")
            java.text.NumberFormat.getCurrencyInstance(loc).format(totalAdeudado)
        } catch (_: Exception) {
            "$" + String.format("%,.2f", totalAdeudado)
        }
}
