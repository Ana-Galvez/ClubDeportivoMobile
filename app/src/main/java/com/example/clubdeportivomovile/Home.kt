package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.data.DBHelper


class Home : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerContainer: android.view.View
    val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottomBar("inicio")

        drawerLayout = findViewById(R.id.drawer_layout)
        setupDrawerMenu(R.id.drawer_layout) ///********** agregue para fc del menu ---va el id como parametro

        drawerContainer = findViewById(R.id.drawerContainer)

        val menuIcon: ImageView? = findViewById(R.id.img_menu_hamburguesa)
        menuIcon?.setOnClickListener {
            // Abre por vista (más seguro que por gravedad)
            drawerLayout.openDrawer(drawerContainer)
        }

        // Manejo de back para cerrar el drawer
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        //Manejo del nombre del usuario
        val tvBienvenida : TextView = findViewById(R.id.tvBienvenida)
        val usuario = intent.getStringExtra("usuario") ?: "Usuario"
        tvBienvenida.text = "Bienvenido $usuario"

        // Navegación de los botones

        findViewById<LinearLayout>(R.id.btnRegistrarCliente)?.setOnClickListener{
            val dialog = AptoFisico()
            dialog.show(supportFragmentManager, "AptoDialog")

        }

        findViewById<LinearLayout>(R.id.btnListadoClientes)?.setOnClickListener{
            val intent= Intent(this,ListadoDeClientes::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.btnRegistroPago)?.setOnClickListener {
            val dialog = SocioONoSocio()
            dialog.show(supportFragmentManager, "SocioDialog")
        }

        // Restringuir el acceso
        findViewById<LinearLayout>(R.id.btnSociosMorosos)?.setOnClickListener {
            val dbHelper = DBHelper(this)
            val usuarioActual = intent.getStringExtra("usuario") ?: ""
            val rol = dbHelper.obtenerRolUsuario(usuarioActual)

            if (rol == 120) {
                val intent = Intent(this, MorososActivity::class.java)
                startActivity(intent)
            } else {
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Acceso restringido")
                    .setMessage("Solo el personal autorizado puede acceder a esta sección.")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
        }
    }
}
