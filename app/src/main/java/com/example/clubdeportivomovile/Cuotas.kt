package com.example.clubdeportivomovile

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Cuotas(
    val IdCuota: Int,
    val idCliente: Int,
    val Monto: Double,
    val ModoPago: String?, // Es String? (nullable) porque no tiene NOT NULL en la DB
    val Estado: String,
    val FechaPago: String?, // Es String? (nullable) porque no tiene NOT NULL en la DB
    val FechaVencimiento: String,
    val CantCuotas: Int,
    val UltDigitosTarj: Int
) : Serializable{
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
    val fechaVencimientoUI: String
        get() = formatearFecha(FechaVencimiento)
}


