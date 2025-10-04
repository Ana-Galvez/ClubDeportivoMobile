package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class cerrarsesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cerrarsesion)

        val btnSi = findViewById<Button>(R.id.btn_si)
        val btnNo = findViewById<Button>(R.id.btn_no)

        btnSi.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finishAffinity()
        }

        btnNo.setOnClickListener {
            // Solo cierra el diálogo
            finish()
        }
    }

}