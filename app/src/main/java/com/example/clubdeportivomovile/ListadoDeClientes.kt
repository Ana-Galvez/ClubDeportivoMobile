package com.example.clubdeportivomovile

import android.os.Bundle
import android.content.Intent
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date


class ListadoDeClientes : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerClientes: RecyclerView
    private lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_de_clientes)

        drawerLayout = findViewById(R.id.drawerLayout)
        recyclerClientes = findViewById(R.id.recyclerClientes)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout)
        setupBottomBar("clientes")

        // Datos
        val listaClientes = listOf(
            Cliente(
                1,
                "María",
                "Golden",
                "10/05/1990",
                12345678,
                "F",
                "Calle Falsa 01",
                "09012345",
                "02/07/2024",
                true,
                false
            ),
            Cliente(
                2,
                "María",
                "Silver",
                "10/05/1990",
                12345679,
                "F",
                "Callejón New",
                "523112345",
                "30/10/2024",
                false,
                false
            ),
            Cliente(
                3,
                "Juan",
                "Chavo",
                "15/03/1985",
                23456789,
                "Calle NoHay 10",
                "M",
                "01112345",
                "15/04/2025",
                true,
                true
            ),
            Cliente(
                4,
                "Lorena",
                "Sim",
                "22/11/1992",
                34567890,
                "F",
                "Calle S/N",
                "04662345",
                "22/12/2024",
                true,
                false
            ),
            Cliente(
                5,
                "Armando",
                "Perez Gomez",
                "01/08/1978",
                45678901,
                "M",
                "Calle Nueva",
                "222662345",
                "13/12/2024",
                false,
                false
            ),
            Cliente(
                6,
                "Armando",
                "Perez Gomez",
                "01/08/1978",
                45678901,
                "M",
                "Calle Nueva",
                "222662345",
                "13/12/2024",
                false,
                false
            ),
            Cliente(
                7,
                "Armando",
                "Perez Gomez",
                "01/08/1978",
                45678901,
                "M",
                "Calle Nueva",
                "222662345",
                "13/12/2024",
                false,
                false
            ),
            Cliente(
                8,
                "Armando",
                "Perez Gomez",
                "01/08/1978",
                45678901,
                "M",
                "Calle Nueva",
                "222662345",
                "13/12/2024",
                false,
                false
            ),
            Cliente(
                9,
                "Armando",
                "Perez Gomez",
                "01/08/1978",
                45678901,
                "M",
                "Calle Nueva",
                "222662345",
                "13/12/2024",
                false,
                false
            )
        )

        adapter = ClienteAdapter(
            clientes = listaClientes,
            onEditar = { onEditarClienteClick(it) },
            onMostrarCarnet = { onMostrarCarnetClick(it) },
            onEliminar = { onEliminarClienteClick(it) },
            onRegistrarPago = { onRegistrarPagoClick(it) }
        )

        recyclerClientes.layoutManager = LinearLayoutManager(this)
        recyclerClientes.adapter = adapter
    }

    private fun onEditarClienteClick(cliente: Cliente) {
        val intent = Intent(this, EditarClienteActivity::class.java)
        intent.putExtra("nombre", cliente.nombre)
        intent.putExtra("apellido", cliente.apellido)
        intent.putExtra("id", cliente.id)
        intent.putExtra("fechaNacimiento", cliente.fechaNacimiento)
        intent.putExtra("fechaInscripcion", cliente.fechaInscripcion)
        intent.putExtra("direccion", cliente.direccion)
        intent.putExtra("dni", cliente.dni)
        intent.putExtra("telefono", cliente.telefono)
        intent.putExtra("genero", cliente.genero)
        intent.putExtra("socio", cliente.socio)
        startActivity(intent)
    }

    private fun onMostrarCarnetClick(cliente: Cliente) {
        val nombreCompleto = cliente.nombre + " " + cliente.apellido
        val intent = Intent(this, CarnetActivity::class.java) // <-- Crear el Intent aquí
        intent.putExtra("nombreCompleto", nombreCompleto)
        intent.putExtra("id", cliente.id)
        intent.putExtra("direccion", cliente.direccion)
        intent.putExtra("dni", cliente.dni)
        intent.putExtra("telefono", cliente.telefono)
        intent.putExtra("socio", cliente.socio)
        intent.putExtra("fechaInscripcion", cliente.fechaInscripcion)
        startActivity(intent)
    }

    //TODO: conectar al eliminar
    private fun onEliminarClienteClick(cliente: Cliente) {
        val modal = EliminarCliente()
        modal.show(supportFragmentManager, "modalEliminar")
    }

    //TODO: Al ir a los formularios mandar el nombre del cliente y que no sea buscador
    private fun onRegistrarPagoClick(cliente: Cliente) {
        if (cliente.socio.equals(true)) {
            // socio, lo lleva al formulario de pago de socios
            val intent = Intent(this, ReciboPagoSocio::class.java)
            startActivity(intent)
        } else {
            // no socio, lo lleva al formulario de pago de no socios
            val intent = Intent(this, PagoNoSocioActivity::class.java)
            startActivity(intent)
        }
    }

}