package com.example.clubdeportivomovile


import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout

class EditarClienteActivity : BaseActivity() { //cambiamos de quien hereda asi tiene la fc de la barra
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupBottomBar("")  //activo botones barra, cadena vacia para q no resalte ningun boton de la barra
    }
}