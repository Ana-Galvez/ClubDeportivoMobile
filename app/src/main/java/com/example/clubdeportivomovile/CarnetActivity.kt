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
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout_carnet)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val root: LinearLayout = findViewById(R.id.container_carnet)
        root.setPadding(0, 0, 0, 0)

        // Drawer y botones
        drawerLayout = findViewById(R.id.drawer_layout_carnet)
        menuHamburguesa = findViewById(R.id.img_menu_hamburguesa)
        flechaAtras = findViewById(R.id.back_carnet)

        menuHamburguesa.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        flechaAtras.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        setupBottomBar("")  //activo botones barra, cadena vacia para q no resalte ningun boton de la barra
    }
}