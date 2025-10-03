package com.example.clubdeportivomovile


import android.app.Dialog
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

abstract class BaseActivity : AppCompatActivity() {

    protected fun setupHeader(drawerLayout: DrawerLayout? = null) {
        val backBtn: ImageView? = findViewById(R.id.ivBack)
        val menuBtn: ImageView? = findViewById(R.id.ivMenu)

        // Acción del botón atrás
        backBtn?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Acción del menú hamburguesa
        menuBtn?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }
    protected fun setupBottomBar(current: String) {
    /* current: nombre de pantalla activa*/
        findViewById<LinearLayout>(R.id.llInicio)?.setOnClickListener {
            if (this !is Home) {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
        }
        findViewById<LinearLayout>(R.id.llNuevo)?.setOnClickListener {
            if (this !is registrar) {
                startActivity(Intent(this, registrar::class.java))
                finish()
            }
        }

        findViewById<LinearLayout>(R.id.llClientes)?.setOnClickListener {
            if (this !is ListadoDeClientes) {
                startActivity(Intent(this, ListadoDeClientes::class.java))
                finish()
            }
        }
        findViewById<LinearLayout>(R.id.llPagos)?.setOnClickListener {
            val dialog = SocioONoSocio()
            dialog.show(supportFragmentManager, "SocioDialog")
        }

        findViewById<LinearLayout>(R.id.llMorosos)?.setOnClickListener {
            if (this !is MorososActivity) {
                startActivity(Intent(this, MorososActivity::class.java))
                finish()
            }
        }

        // Por defecto todos en negro
        findViewById<ImageView>(R.id.ivInicio)?.setImageResource(R.drawable.img_inicio_negro)
       // findViewById<ImageView>(R.id.ivNuevo)?.setImageResource(R.drawable.img_nuevo_negro)
        findViewById<ImageView>(R.id.ivClientes)?.setImageResource(R.drawable.img_clientes_negro)
        findViewById<ImageView>(R.id.ivPagos)?.setImageResource(R.drawable.img_pagos_negro)
        findViewById<ImageView>(R.id.ivMorosos)?.setImageResource(R.drawable.img_morosos_negro)

        // imagen en blanco en la actual
        when (current) {
            "inicio"   -> findViewById<ImageView>(R.id.ivInicio)?.setImageResource(R.drawable.img_inicio_blanco)
            //"nuevo"   -> findViewById<ImageView>(R.id.ivNuevo)?.setImageResource(R.drawable.img_inicio_blanco)
            "clientes" -> findViewById<ImageView>(R.id.ivClientes)?.setImageResource(R.drawable.img_clientes_blanco)
            "pagos"    -> findViewById<ImageView>(R.id.ivPagos)?.setImageResource(R.drawable.img_pagos_blanco)
            "morosos"  -> findViewById<ImageView>(R.id.ivMorosos)?.setImageResource(R.drawable.img_morosos_blanco)
        }

    }
}