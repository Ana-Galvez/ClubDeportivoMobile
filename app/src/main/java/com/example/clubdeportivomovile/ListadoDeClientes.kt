package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
            Cliente("Pedro Pérez", "Socio"),
            Cliente("María López", "No socio"),
            Cliente("Carlos Gómez", "Socio"),
            Cliente("Pedro Pérez", "Socio"),
            Cliente("María López", "No socio"),
            Cliente("Carlos Gómez", "Socio"),
            Cliente("Pedro Pérez", "Socio"),
            Cliente("María López", "No socio"),
            Cliente("Carlos Gómez", "Socio"),
            Cliente("Pedro Pérez", "Socio"),
            Cliente("María López", "No socio"),
            Cliente("Carlos Gómez", "Socio"),
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
        startActivity(Intent(this, EditarClienteActivity::class.java))
    }

    private fun onMostrarCarnetClick(cliente: Cliente) {
        startActivity(Intent(this, CarnetActivity::class.java))
    }

    private fun onEliminarClienteClick(cliente: Cliente) {
        val modal = EliminarCliente()
        modal.show(supportFragmentManager, "modalEliminar")
    }

    private fun onRegistrarPagoClick(cliente: Cliente) {
        if (cliente.tipo.equals("Socio", ignoreCase = true)) {
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