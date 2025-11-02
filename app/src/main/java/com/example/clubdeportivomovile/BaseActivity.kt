package com.example.clubdeportivomovile


import android.content.Intent
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

/* clase base para definir funcionalidades comunes a todas las pantallas*/
abstract class BaseActivity : AppCompatActivity() {

    //Header
    protected fun setupHeader(drawerLayout: DrawerLayout? = null) {
        val backBtn: ImageView? = findViewById(R.id.ivBack)
        val menuBtn: ImageView? = findViewById(R.id.ivMenu)

        // Botón atrás
        backBtn?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Menú hamburguesa
        menuBtn?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    //Barra inferior
    //current: nombre de pantalla activa, puede estar vacia
    protected fun setupBottomBar(current: String) {

        //inicio
        findViewById<LinearLayout>(R.id.llInicio)?.setOnClickListener {
            if (this !is Home) {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
        }
        //nuevo
        findViewById<LinearLayout>(R.id.llNuevo)?.setOnClickListener {
            if (this !is Registrar) {
                val dialog = AptoFisico()
                dialog.show(supportFragmentManager, "AptoDialog")
            }
        }
        //Clientes
        findViewById<LinearLayout>(R.id.llClientes)?.setOnClickListener {
            if (this !is ListadoDeClientes) {
                startActivity(Intent(this, ListadoDeClientes::class.java))
                finish()
            }
        }
        //pagos
        findViewById<LinearLayout>(R.id.llPagos)?.setOnClickListener {
            val dialog = SocioONoSocio()
            dialog.show(supportFragmentManager, "SocioDialog")
        }

        //Lista de morososo o vencimientos
        findViewById<LinearLayout>(R.id.llMorosos)?.setOnClickListener {
            if (this !is MorososActivity) {
                startActivity(Intent(this, MorososActivity::class.java))
                finish()
            }
        }

        // Por defecto todos en negro
        findViewById<ImageView>(R.id.ivInicio)?.setImageResource(R.drawable.img_inicio_negro)
        findViewById<ImageView>(R.id.ivNuevo)?.setImageResource(R.drawable.img_nuevo_negro)
        findViewById<ImageView>(R.id.ivClientes)?.setImageResource(R.drawable.img_clientes_negro)
        findViewById<ImageView>(R.id.ivPagos)?.setImageResource(R.drawable.img_pagos_negro)
        findViewById<ImageView>(R.id.ivMorosos)?.setImageResource(R.drawable.img_morosos_negro)

        // Cambiamos a la imagen en blanco en la actual
        when (current) {
            "inicio" -> {
                findViewById<ImageView>(R.id.ivInicio)?.setImageResource(R.drawable.img_inicio_blanco)
                findViewById<TextView>(R.id.tvInicio)?.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "nuevo" -> {
                findViewById<ImageView>(R.id.ivNuevo)?.setImageResource(R.drawable.img_nuevo_blanco)
                findViewById<TextView>(R.id.tvNuevo)?.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "clientes" -> {
                findViewById<ImageView>(R.id.ivClientes)?.setImageResource(R.drawable.img_clientes_blanco)
                findViewById<TextView>(R.id.tvClientes)?.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "pagos" -> {
                findViewById<ImageView>(R.id.ivPagos)?.setImageResource(R.drawable.img_pagos_blanco)
                findViewById<TextView>(R.id.tvPagos)?.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "morosos" -> {
                findViewById<ImageView>(R.id.ivMorosos)?.setImageResource(R.drawable.img_morosos_blanco)
                findViewById<TextView>(R.id.tvMorosos)?.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }

    }

    // Barra lateral
    // drawerId: nombre del Drawer de nuestra pantalla
    protected fun setupDrawerMenu(drawerId: Int) {
        val drawer = findViewById<DrawerLayout?>(drawerId)

        // Inicio
        //findViewById<TextView?>(R.id.txtInicio)?.setOnClickListener {
        //    drawer?.closeDrawer(GravityCompat.START)
        //}

        // App físico -> Sitio en Construcción
        findViewById<TextView?>(R.id.txtAppFisico)?.setOnClickListener {
            startActivity(Intent(this, Sitioconstruccion::class.java))
            drawer?.closeDrawer(GravityCompat.START)
        }

        // Ayuda -> Sitio Caido
        findViewById<TextView?>(R.id.txtAyuda)?.setOnClickListener {
            startActivity(Intent(this, Sitiocaido::class.java))
            drawer?.closeDrawer(GravityCompat.START)
        }

        // Configuración -> Configuracion
        findViewById<TextView?>(R.id.txtConfiguracion)?.setOnClickListener {
            startActivity(Intent(this, Sitiocaido::class.java))
            drawer?.closeDrawer(GravityCompat.START)
        }

        // Cerrar sesión
        findViewById<TextView?>(R.id.txtCerrarSesion)?.setOnClickListener {
            startActivity(Intent(this, Cerrarsesion::class.java))
            drawer?.closeDrawer(GravityCompat.START)
        }
    }
}