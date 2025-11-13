package com.example.clubdeportivomovile

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

// Al tener más de 2 datos utilizo Data Class
data class PagoActividad(
    val id: Int,
    val idCliente: Int,
    val idActividad: Int,
    val fechaPago: String,
    //val modoPago: String,
    val monto: Double,
    val estado: String,
) : Serializable {
    //Formatear fecha de la DB
    private val formatoDb = "yyyy-MM-dd"
    private val formatoUi = "dd/MM/yyyy"
    private fun formatearFecha(fechaStr: String): String {
        if (fechaStr.isEmpty() || fechaStr == "Sin fecha") {
            return fechaStr
        }

        return try {
            val parser = SimpleDateFormat(formatoDb, Locale.getDefault())
            val formatter = SimpleDateFormat(formatoUi, Locale.getDefault())

            formatter.format(parser.parse(fechaStr))

        } catch (e: Exception) {
            fechaStr
        }
    }

    //Propiedades a utilizar con fecha formateada
    val fechaPagoUI: String
        get() = formatearFecha(fechaPago)
}