package com.example.clubdeportivomovile

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.Spinner

class PagoNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_pago_no_socios)

        val spinnerActividad: Spinner = findViewById(R.id.spinner_pago_no_socio)
        val items = resources.getStringArray(R.array.actividades).toList()

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_custom,
            items
        )

        adapter.setDropDownViewResource(R.layout.spinner_item_custom)

        spinnerActividad.adapter = adapter
        }
    }
