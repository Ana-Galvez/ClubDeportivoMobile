package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class Home : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerContainer: android.view.View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottomBar("inicio")

        drawerLayout = findViewById(R.id.drawer_layout)
        setupDrawerMenu(R.id.drawer_layout) ///********** agregue para fc del menu ---va el id como parametro

       /* val menuIcon: ImageView? = findViewById(R.id.img_menu_hamburguesa)

        menuIcon?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }

        // Back cierra el drawer si está abierto
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                drawerLayout?.let { dl ->
                    if (dl.isDrawerOpen(GravityCompat.START)) {
                        dl.closeDrawer(GravityCompat.START)
                        return
                    }
                }
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        })*/
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
        findViewById<TextView>(R.id.txtInicio)?.setOnClickListener {
            drawerLayout.closeDrawer(drawerContainer)
        }

        // Navegación de los botones

        findViewById<LinearLayout>(R.id.btnRegistrarCliente)?.setOnClickListener{
            val intent= Intent(this,Registrar::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.btnListadoClientes)?.setOnClickListener{
            val intent= Intent(this,ListadoDeClientes::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.btnRegistroPago)?.setOnClickListener {
            val dialog = SocioONoSocio()
            dialog.show(supportFragmentManager, "SocioDialog")
        }

        findViewById<LinearLayout>(R.id.btnSociosMorosos)?.setOnClickListener{
            val intent= Intent(this, MorososActivity::class.java)
            startActivity(intent)
        }
    }
}
