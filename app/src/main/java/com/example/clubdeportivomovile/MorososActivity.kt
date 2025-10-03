package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout

class MorososActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.activity_morosos)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        drawerLayout = findViewById(R.id.drawerLayout)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)

        setupBottomBar("morosos")  //activo botones barra
    }
}