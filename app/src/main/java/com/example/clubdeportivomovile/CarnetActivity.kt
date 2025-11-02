package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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

    // Datos del carnet
    val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)
    val tvId = findViewById<TextView>(R.id.tvId)
    val tvDni = findViewById<TextView>(R.id.tvDni)
    val tvSocio = findViewById<TextView>(R.id.tvSocio)
    val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
    val tvTelefono = findViewById<TextView>(R.id.tvTelefono)
    val tvInsc = findViewById<TextView>(R.id.tvFechaInsc)

    val nombreCliente = intent.getStringExtra("nombreCompleto")
    val id = intent.getIntExtra("id", -1)
    val dni = intent.getIntExtra("dni", -1)
    val socio = intent.getBooleanExtra("socio", false)
    val direccion = intent.getStringExtra("direccion")
    val telefono = intent.getStringExtra("telefono")
    val inscrip = intent.getStringExtra("fechaInscripcion")

    tvNombreSocio.text = nombreCliente
    tvId.text = id.toString()
    tvDni.text = dni.toString()
    tvSocio.text = if(socio) "Sí" else "No"
    tvDireccion.text = direccion
    tvTelefono.text = telefono
    tvInsc.text = inscrip
    }
}