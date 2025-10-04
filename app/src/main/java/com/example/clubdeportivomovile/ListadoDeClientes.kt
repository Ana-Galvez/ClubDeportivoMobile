package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.TextView

class ListadoDeClientes : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_listado_de_clientes)
       drawerLayout = findViewById(R.id.drawerLayout)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("clientes")  //activo botones barra

    }
    // Navegación botones derecha del cliente
    fun onEditarClienteClick(view: View) {
        val i = Intent(this, EditarClienteActivity::class.java)
        startActivity(i)
    }
    fun onMostrarCarnetClick(view: View) {
        val i = Intent(this, CarnetActivity::class.java)
        startActivity(i)
    }
    fun onEliminarClienteClick(view: View) {
        val modal = EliminarCliente()
        modal.show(supportFragmentManager, "modalEliminar")
    }

    fun onRegistrarPagoClick(view: View) {
        //Cuando esté el debe dirigir al formulario de pago correspondiente al tipo de cliente
            val i = Intent(this, ReciboPagoSocio::class.java)
            startActivity(i)
    }
}