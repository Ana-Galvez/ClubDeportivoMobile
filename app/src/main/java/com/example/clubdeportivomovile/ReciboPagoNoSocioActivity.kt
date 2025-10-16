package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout

class ReciboPagoNoSocioActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_pago_no_socio)

        drawerLayout = findViewById(R.id.drawerLayout)

        //  header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) /// menu ---va el id como parametro
        setupBottomBar("")//barra

        // Datos del formulario
        val nombreCliente = intent.getStringExtra("nombreCliente")
        val actividad = intent.getStringExtra("actividad")
        val horario = intent.getStringExtra("horario")
        val monto = intent.getStringExtra("monto")
        val direccion = intent.getStringExtra("direccion")
        val telefono = intent.getStringExtra("telefono")
        val inscrip = intent.getStringExtra("ins")

        val tvNombreSocio = findViewById<TextView>(R.id.tvNoSocioNombre)
        val tvActividad = findViewById<TextView>(R.id.tvNoSocioActividad)
        val tvMonto = findViewById<TextView>(R.id.tvNoSocioMonto)
        val tvHorario = findViewById<TextView>(R.id.tvNoSocioHorario)
        val tvDireccion = findViewById<TextView>(R.id.tvNoSocioDireccion)
        val tvTelefono = findViewById<TextView>(R.id.tvNoSocioTelefono)
        val tvInsc = findViewById<TextView>(R.id.tvNoSocioFechaInsc)

        tvNombreSocio.text = nombreCliente
        tvActividad.text = actividad
        tvMonto.text = "$$monto"
        tvHorario.text = horario
        tvDireccion.text = direccion
        tvTelefono.text = telefono
        tvInsc.text = inscrip
    }

}