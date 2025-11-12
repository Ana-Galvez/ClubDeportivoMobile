package com.example.clubdeportivomovile

import android.os.Bundle
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivomovile.data.DBHelper
import com.example.clubdeportivomovile.data.DBHelper.Companion.RESULT_HAS_DEBT


class ListadoDeClientes : BaseActivity(), EliminacionClienteListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerClientes: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private lateinit var layoutBuscador: LinearLayout
    private lateinit var tvSinClientes: TextView
    private lateinit var listaClientes: MutableList<Cliente>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_de_clientes)

        drawerLayout = findViewById(R.id.drawerLayout)
        recyclerClientes = findViewById(R.id.recyclerClientes)
        layoutBuscador = findViewById(R.id.layout_buscador)
        tvSinClientes = findViewById(R.id.tv_sin_clientes)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout)
        setupBottomBar("clientes")

        // Datos
        val dbHelper = DBHelper(this)

        listaClientes = dbHelper.obtenerClientes().toMutableList()

        //MEnsaje lista de clientes vacia
        if (listaClientes.isEmpty()) {
            recyclerClientes.visibility = View.GONE
            tvSinClientes.visibility = View.VISIBLE
            //Deshabilito el buscador
            layoutBuscador.visibility = View.GONE
            return
        }

        adapter = ClienteAdapter(
            clientes = listaClientes,
            onEditar = { onEditarClienteClick(it) },
            onMostrarCarnet = { onMostrarCarnetClick(it) },
            onEliminar = { onEliminarClienteClick(it) },
            onRegistrarPago = { onRegistrarPagoClick(it) }
        )
        recyclerClientes.layoutManager = LinearLayoutManager(this)
        recyclerClientes.adapter = adapter

        //Buscador
        val buscador: AutoCompleteTextView = findViewById(R.id.etBuscar)
        val nombresClientes = listaClientes.map { it.Nombre + " " + it.Apellido }

        val adapterClientes = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresClientes
        )

        // Para mostrar sugerencia de busqueda de nombres
        buscador.setAdapter(adapterClientes)

        buscador.addTextChangedListener { texto ->
            val query = texto.toString()

            if (query.isEmpty()) {
                // mostrar todos
                adapter.updateList(listaClientes)
            }
        }

        // cuando elige un nombre
        buscador.setOnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = parent.getItemAtPosition(position) as String

            val clienteEncontrado =
                listaClientes.find { "${it.Nombre} ${it.Apellido}" == nombreSeleccionado }

            if (clienteEncontrado != null) {
                adapter.updateList(listOf(clienteEncontrado))
            }
        }
    }

private fun onEditarClienteClick(cliente: Cliente) {
    val intent = Intent(this, EditarClienteActivity::class.java)
    intent.putExtra("nombre", cliente.Nombre)
    intent.putExtra("apellido", cliente.Apellido)
    intent.putExtra("nombreCompleto", cliente.nombreCompleto)
    intent.putExtra("id", cliente.id)
    intent.putExtra("fechaNacimiento", cliente.fechaNacimientoUI)
    intent.putExtra("fechaInscripcion", cliente.fechaInscripcionUI)
    intent.putExtra("direccion", cliente.Direccion)
    intent.putExtra("dni", cliente.DNI)
    intent.putExtra("telefono", cliente.Telefono)
    intent.putExtra("genero", cliente.Genero)
    intent.putExtra("socio", cliente.Socio)
    startActivity(intent)
}

private fun onMostrarCarnetClick(cliente: Cliente) {
    val intent = Intent(this, CarnetActivity::class.java)
    intent.putExtra("nombreCompleto", cliente.nombreCompleto)
    intent.putExtra("id", cliente.id)
    intent.putExtra("direccion", cliente.Direccion)
    intent.putExtra("dni", cliente.DNI)
    intent.putExtra("telefono", cliente.Telefono)
    intent.putExtra("socio", cliente.esSocio)
    intent.putExtra("fechaInscripcion", cliente.fechaInscripcionUI)
    startActivity(intent)
}
//Eliminar clientes (socios solo con todas las cuotas pagadas)
override fun onClienteEliminado(idCliente: Int, nombreCliente: String) {
    val dbHelper = DBHelper(this)
    val resultado = dbHelper.eliminarClientePorId(idCliente)

    when (resultado) {
        DBHelper.RESULT_OK -> {
            Toast.makeText(this, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show()

            listaClientes.removeAll { it.id == idCliente }
            adapter.updateList(listaClientes)
        }
        DBHelper.RESULT_HAS_DEBT -> {
            Toast.makeText(this, "No se puede eliminar: el cliente tiene cuotas pendientes", Toast.LENGTH_LONG).show()
        }
        DBHelper.RESULT_ERROR -> {
            Toast.makeText(this, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show()
        }
    }
}

   override fun onEliminacionCancelada() {
        Toast.makeText(this, "Eliminación cancelada.", Toast.LENGTH_SHORT).show()
    }

    private fun recargarListaClientes() {
        val dbHelper = DBHelper(this)
        listaClientes.clear()
        listaClientes.addAll(dbHelper.obtenerClientes())

        adapter.updateList(listaClientes)

        if (listaClientes.isEmpty()) {
            recyclerClientes.visibility = View.GONE
            tvSinClientes.visibility = View.VISIBLE
            layoutBuscador.visibility = View.GONE
        } else {
            recyclerClientes.visibility = View.VISIBLE
            tvSinClientes.visibility = View.GONE
            layoutBuscador.visibility = View.VISIBLE
        }
        Log.d("DEBUG_LISTA", "Lista actualizada: ${listaClientes.size} clientes.")
    }
private fun onEliminarClienteClick(cliente: Cliente) {
    //PAso los datos a eliminar
    val modal = EliminarCliente.newInstance(cliente.id, cliente.nombreCompleto)
    modal.show(supportFragmentManager, "modalEliminar")
}

private fun onRegistrarPagoClick(cliente: Cliente) {
    if (cliente.esSocio) {
        // socio, lo lleva al formulario de pago de socios con el nombre y id
        val intent = Intent(this, RegistroPagoSocio::class.java)
        intent.putExtra("clienteSeleccionado", cliente.nombreCompleto)
        intent.putExtra("id", cliente.id)
        startActivity(intent)
    } else {
        // no socio, lo lleva al formulario de pago de no socios con el nombre y id
        val intent = Intent(this, PagoNoSocioActivity::class.java)
        intent.putExtra("clienteSeleccionado", cliente.nombreCompleto)
        intent.putExtra("id", cliente.id)
        startActivity(intent)
    }
}

}