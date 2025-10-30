package com.example.clubdeportivomovile


import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout

class Sitioconstruccion : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitioconstruccion)
        drawerLayout = findViewById(R.id.drawerLayout_sitioConstruccion)
        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout_sitioCaido) ///********** agregue para fc del menu ---va el id como parametro

    }
}