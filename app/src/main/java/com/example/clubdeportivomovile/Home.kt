package com.example.clubdeportivomovile

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class Home : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottomBar("inicio")

        drawerLayout = findViewById(R.id.drawerLayout)
        val menuIcon: ImageView? = findViewById(R.id.img_menu_hamburguesa)

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
        })
    }
}
