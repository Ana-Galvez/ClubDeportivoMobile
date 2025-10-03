package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
    }
}
