package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class PagoNoSocioActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuHamburguesa: ImageView
    private lateinit var flechaAtras: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_pago_no_socios)
        setupDrawerMenu(R.id.drawer_layout_no_socio) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")

        // Spinner
        val spinnerActividad: Spinner = findViewById(R.id.spinner_pago_no_socio)
        val items = resources.getStringArray(R.array.actividades).toList()
        val adapter = ArrayAdapter(this, R.layout.spinner_item_custom, items)
        adapter.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerActividad.adapter = adapter

        // Drawer y botones
        drawerLayout = findViewById(R.id.drawer_layout_no_socio)
        setupHeader(drawerLayout)

        val botonAceptar: Button = findViewById(R.id.BotonAceptarPagoNoSocio)

        botonAceptar.setOnClickListener {
            val intent = Intent(this, ReciboPagoNoSocioActivity::class.java)
            startActivity(intent)
        }

        val botonLimpiar: Button = findViewById(R.id.BotonLimpiarPagoNoSocio)

        botonLimpiar.setOnClickListener {
            //cambiar por borrar formulario
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        setupBottomBar("pagos")  //activo botones barra
    }
}

