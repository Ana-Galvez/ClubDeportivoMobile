package com.example.clubdeportivomovile


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.drawerlayout.widget.DrawerLayout

class EditarClienteActivity : BaseActivity() { //cambiamos de quien hereda asi tiene la fc de la barra
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)
        drawerLayout = findViewById(R.id.drawerLayout)

        // header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) /// menu ---va el id como parametro
        setupBottomBar("")  //barra, cadena vacia para q no resalte ningun boton de la barra

        val botonAceptar: Button = findViewById(R.id.btnGuardar)

        botonAceptar.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        val botonLimpiar: Button = findViewById(R.id.btnLimpiar)
        botonLimpiar.setOnClickListener {
            //cambiar por borrar formulario
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}