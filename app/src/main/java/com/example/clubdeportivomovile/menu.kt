package com.example.clubdeportivomovile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los TextView
        val inicio = findViewById<TextView>(R.id.txtInicio)
        val appFisico = findViewById<TextView>(R.id.txtAppFisico)
        val ayuda = findViewById<TextView>(R.id.txtAyuda)
        val configuracion = findViewById<TextView>(R.id.txtConfiguracion)


        // Todos abren Sitio en construcción
        val intentSitio = Intent(this, sitioconstruccion::class.java)

        inicio.setOnClickListener { startActivity(intentSitio) }
        appFisico.setOnClickListener { startActivity(intentSitio) }
        ayuda.setOnClickListener { startActivity(intentSitio) }
        configuracion.setOnClickListener { startActivity(intentSitio) }

    }

    private fun mostrarDialogoSalir() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_cerrarsesion) // tu modal custom
        dialog.setCancelable(false)

        val btnSi = dialog.findViewById<TextView>(R.id.btn_si)
        val btnNo = dialog.findViewById<TextView>(R.id.btn_no)

        btnSi.setOnClickListener {
            dialog.dismiss()
            finishAffinity() // Cierra toda la app
        }

        btnNo.setOnClickListener {
            dialog.dismiss() // Solo cierra el modal
        }

        dialog.show()
    }
}
