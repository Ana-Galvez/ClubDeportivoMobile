package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.utils.DescargarRecibo

class ReciboSocioActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    val descargarRecibo = DescargarRecibo(this)

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
        val cuota = intent.getStringExtra("cuotaPendiente")
        val cuotaFormateada = intent.getStringExtra("cuotaFormateada")
        val monto = intent.getStringExtra("monto")
        val formaPago = intent.getStringExtra("formaPago")
        val cuotaTarjeta = intent.getStringExtra("cantCuotasTarjeta")
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
        tvCuota.text = cuotaFormateada
        tvMonto.text = "$$monto"
        tvFormaPago.text = if (!cuotaTarjeta.isNullOrEmpty() && cuotaTarjeta != "Seleccionar...") {
            "$formaPago $cuotaTarjeta"
        } else {
            "$formaPago"
        }
        tvDireccion.text = direccion
        tvTelefono.text = telefono
        tvInsc.text = inscrip

        // Mostrar alerta de éxito
        fun mostrarAlertaExito() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Descarga completada")
            builder.setMessage("Se descargó el recibo con éxito.")
            builder.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }

        //Descargar rebibo btnExportar
        val btnExportar = findViewById<Button>(R.id.btnExportar)

        btnExportar.setOnClickListener {
            descargarRecibo.exportarRecibo(
                monto = monto!!,
                nombreCliente = nombreCliente!!,
                inscrip = inscrip!!,
                telefono = telefono!!,
                direccion = direccion!!,
                cuota = cuota,
                formaPago = formaPago,
                onSuccess = { mostrarAlertaExito() }
            )
        }
    }
}