package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout

class ReciboPagoSocio : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_pago_socio)

        val spinnerPago: Spinner = findViewById(R.id.spinner_pago)

        // Crea un ArrayAdapter usando el string-array y un layout simple
        ArrayAdapter.createFromResource(
            this,
            R.array.opciones_pago,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Especifica el layout para usar cuando la lista de opciones aparece
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Aplica el adaptador al spinner
            spinnerPago.adapter = adapter
        }

        drawerLayout = findViewById(R.id.drawerLayout)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")  //activo botones barra

        val botonAceptar: Button = findViewById(R.id.btnAceptarSocio)

        botonAceptar.setOnClickListener {
            val intent = Intent(this, ReciboSocioActivity::class.java)
            startActivity(intent)
        }

        val botonLimpiar: Button = findViewById(R.id.btnLimpiarSocio)

        botonLimpiar.setOnClickListener {
            //cambiar por borrar formulario
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}