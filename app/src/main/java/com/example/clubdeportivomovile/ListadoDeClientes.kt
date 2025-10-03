package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.content.Intent
import android.widget.ImageView
import android.widget.LinearLayout

class ListadoDeClientes : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_listado_de_clientes)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        setupBottomBar("clientes")  //activo botones barra

        // Navegación botones derecha del cliente

        val ivVerCliente = findViewById<ImageView>(R.id.ivVerCliente)
        ivVerCliente.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ivRegistrarPago)?.setOnClickListener {
            val dialog = SocioONoSocio()
            dialog.show(supportFragmentManager, "SocioDialog")
        }

       /* val ivEditarCliente = findViewById<ImageView>(R.id.ivEditarCliente)
        ivEditarCliente.setOnClickListener {
            val intent = Intent(this, EditarClienteActivity::class.java)
            startActivity(intent)
        }*/

        //NO FUNCIONA PARA MOSTRAR MODAL DE ELIMINAR VERRR

//        findViewById<ImageView>(R.id.ivEliminarCliente)?.setOnClickListener {
//            val dialog = eliminar_cliente()
//            dialog.show(supportFragmentManager, "EliminarDialog")
//        }
    }
    fun onEditarClienteClick(view: View) {
        val i = Intent(this, EditarClienteActivity::class.java)
        startActivity(i)
    }
}