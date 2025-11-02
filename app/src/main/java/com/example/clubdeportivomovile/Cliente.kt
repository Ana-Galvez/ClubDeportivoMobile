package com.example.clubdeportivomovile

import java.io.Serializable

data class Cliente(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,//TODO:cambiar por Date
    val dni: Int,
    val genero: String,
    val direccion: String,
    val telefono: String,
    val fechaInscripcion: String,//TODO:cambiar por Date
    val aptoFisico: Boolean,
    val socio: Boolean
): Serializable{
    val nombreCompleto: String
        get() = "$nombre $apellido"
}
