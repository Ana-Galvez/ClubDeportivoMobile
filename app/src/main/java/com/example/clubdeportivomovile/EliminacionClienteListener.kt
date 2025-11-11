package com.example.clubdeportivomovile

interface EliminacionClienteListener {
    fun onClienteEliminado(idCliente: Int, nombreCliente: String)
    fun onEliminacionCancelada()
}