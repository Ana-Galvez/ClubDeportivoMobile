package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout

class CarnetActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuHamburguesa: ImageView
    private lateinit var flechaAtras: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)
        drawerLayout = findViewById(R.id.drawer_layout_carnet)
        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawer_layout_carnet) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("nuevo")
    }
}