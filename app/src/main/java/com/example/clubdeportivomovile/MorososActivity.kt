package com.example.clubdeportivomovile

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivomovile.data.DBHelper

class MorososActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerMorosos: RecyclerView
    private lateinit var adapter: MorososAdapter
    private lateinit var layoutBuscador: LinearLayout
    private lateinit var tvSinMorosos: TextView
    private lateinit var listaMorosos: MutableList<Moroso>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morosos)

        drawerLayout = findViewById(R.id.drawerLayout)
        // header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) ///menu ---va el id como parametro
        setupBottomBar("morosos")  //barra

        // Recycler View
        recyclerMorosos = findViewById<RecyclerView>(R.id.rvListaMorosos)
        layoutBuscador = findViewById(R.id.layout_buscador)
        tvSinMorosos = findViewById(R.id.tv_sin_morosos)

        /*val listaMorosos = listOf<MorososClaseRV>(
            MorososClaseRV("Juan Pérez", 1, "11111111", "$40.000", "1123111"),
            MorososClaseRV("Pedro López", 2, "22222222", "$35.000", "1133222"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Mario Torres", 4, "44444444", "$50.000", "1155444")
        )*/

        // Datos
        val dbHelper = DBHelper(this)
        listaMorosos = dbHelper.obtenerMorososDeHoy()
        android.util.Log.d("MorososActivity", "Morosos hoy: ${listaMorosos.size}")

        if (listaMorosos.isEmpty()) {
            recyclerMorosos.visibility = View.GONE
            tvSinMorosos.visibility = View.VISIBLE
            layoutBuscador.visibility = View.GONE
            return
        }

        adapter = MorososAdapter(listaMorosos)
        recyclerMorosos.layoutManager = LinearLayoutManager(this)
        recyclerMorosos.adapter = adapter

        // Buscador
        val buscador: AutoCompleteTextView = findViewById(R.id.etBuscar)
        val nombres = listaMorosos.map { it.nombreCompleto }

        val adapterNombres = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombres
        )
        buscador.setAdapter(adapterNombres)

        // limpiar filtro si se borra el texto
        buscador.addTextChangedListener { texto ->
            if (texto.isNullOrBlank()) {
                adapter.updateList(listaMorosos)
            } else {
                adapter.filtrar(texto.toString())
            }
        }

        // al elegir una sugerencia mostrar sólo ese
        buscador.setOnItemClickListener { parent, _, position, _ ->
            val seleccionado = parent.getItemAtPosition(position) as String
            val m = listaMorosos.find { it.nombreCompleto == seleccionado }
            if (m != null) adapter.updateList(listOf(m))
        }
    }
}