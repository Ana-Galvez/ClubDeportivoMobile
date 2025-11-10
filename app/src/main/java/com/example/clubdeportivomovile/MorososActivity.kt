package com.example.clubdeportivomovile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MorososActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morosos)

        drawerLayout = findViewById(R.id.drawerLayout)

        // header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) ///menu ---va el id como parametro
        setupBottomBar("morosos")  //barra


        // Recycler View
        val recycler = findViewById<RecyclerView>(R.id.rvListaMorosos)

        val listaMorosos = listOf<MorososClaseRV>(
            MorososClaseRV("Juan Pérez", 1, "11111111", "$40.000", "1123111"),
            MorososClaseRV("Pedro López", 2, "22222222", "$35.000", "1133222"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Ana García", 3, "33333333", "$25.000", "1144333"),
            MorososClaseRV("Mario Torres", 4, "44444444", "$50.000", "1155444")
        )

        if(listaMorosos.isEmpty()){
            val dialog = AlertDialog.Builder(this)
                .setTitle("Información")
                .setMessage("No hay morosos para mostrar")
                .setCancelable(false)
                .setPositiveButton("Aceptar") { d, _-> d.dismiss()}
                .create()
            dialog.show()
        } else{
            recycler.layoutManager = LinearLayoutManager(this)
            recycler.adapter = MorososAdapter(listaMorosos)
        }

        // Buscador
        val etBuscar = findViewById<AutoCompleteTextView>(R.id.etBuscar)
        val adapter = MorososAdapter(listaMorosos)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
        })
    }
}