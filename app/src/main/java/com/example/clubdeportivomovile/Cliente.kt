package com.example.clubdeportivomovile

import java.io.Serializable

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
}
