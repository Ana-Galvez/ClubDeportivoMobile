package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout

class Sitiocaido : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuHamburguesa: ImageView
    private lateinit var flechaAtras: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitiocaido)
        drawerLayout = findViewById(R.id.drawerLayout_sitioCaido)
        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout_sitioCaido) ///********** agregue para fc del menu ---va el id como parametro
    }
}