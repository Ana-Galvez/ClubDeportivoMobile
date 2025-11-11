package com.example.clubdeportivomovile

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Cliente(
    val id: Int,
    val Nombre: String,
    val Apellido: String,
    val FechaNacimiento: String,
    val DNI: Int,
    val Genero: String,
    val Direccion: String,
    val Telefono: String,
    val FechaInscripcion: String,
    val AptoFisico: Int,
    val Socio: Int
): Serializable{
    val nombreCompleto: String
        get() = "$Nombre $Apellido"

    val esSocio: Boolean
        get() = Socio == 1

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
    val fechaInscripcionUI: String
        get() = formatearFecha(FechaInscripcion)

    val fechaNacimientoUI: String
        get() = formatearFecha(FechaNacimiento)
}
