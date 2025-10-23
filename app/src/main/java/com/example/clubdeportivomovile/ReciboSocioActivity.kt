package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout

class ReciboSocioActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_socio)
        drawerLayout = findViewById(R.id.drawerLayout)
        //  header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) // menu ---va el id como parametro

        setupBottomBar("pagos")//barra

        // Datos del formulario
        val nombreCliente = intent.getStringExtra("nombreCliente")
        val cuota = intent.getStringExtra("cuota")
        val monto = intent.getStringExtra("monto")
        val formaPago = intent.getStringExtra("formaPago")
        val direccion = intent.getStringExtra("direccion")
        val telefono = intent.getStringExtra("telefono")
        val inscrip = intent.getStringExtra("ins")

        val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)
        val tvCuota = findViewById<TextView>(R.id.tvCuotaSocio)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)
        val tvFormaPago = findViewById<TextView>(R.id.tvFormaPago)
        val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        val tvTelefono = findViewById<TextView>(R.id.tvTelefono)
        val tvInsc = findViewById<TextView>(R.id.tvFechaInsc)

        tvNombreSocio.text = nombreCliente
        tvCuota.text = cuota
        tvMonto.text = "$$monto"
        tvFormaPago.text = formaPago
        tvDireccion.text = direccion
        tvTelefono.text = telefono
        tvInsc.text = inscrip
    }
}