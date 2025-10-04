package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout

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
    }
}