package com.example.clubdeportivomovile


import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.limpiarFormulario

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

        //Botones Limpiar
        val botonLimpiar: Button = findViewById(R.id.btnLimpiar)
        val etNombre= findViewById<EditText>(R.id.etNombre)

        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.contentLayout) // el layout principal del form
            limpiarFormulario(rootLayout)

            // foco al primer campo
            etNombre.requestFocus()
        }

        //Formulario
        //REcibo datos del cliente
        val id = intent.getIntExtra("id", -1)
        //Formulario editar necesita nombre y apellido separado
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""
        //Carnet necesita nombre completo
        val nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        val dni = intent.getIntExtra("dni", 0)
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento")
        val direccion = intent.getStringExtra("direccion")
        val telefono = intent.getStringExtra("telefono")
        val genero = intent.getStringExtra("genero")
        val fechaInscripcion = intent.getStringExtra("fechaInscripcion")

        //Agrego los datos al formulario
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etDni = findViewById<EditText>(R.id.etDni)
        val etFechaNac = findViewById<TextView>(R.id.tvFechaNac)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        //Genero (radio buttons)
        val rbF = findViewById<RadioButton>(R.id.rbF)
        val rbM = findViewById<RadioButton>(R.id.rbM)
        val rbNoDecir = findViewById<RadioButton>(R.id.rbNoDecir)
        //Socio (radio buttons)
        val rbSocioSi = findViewById<RadioButton>(R.id.rbSocioSi)
        val rbSocioNo = findViewById<RadioButton>(R.id.rbSocioNo)
        val socio = intent.getBooleanExtra("socio", false)

        etNombre.setText(nombre)
        etApellido.setText(apellido)
        etDni.setText(dni.toString())
        etFechaNac.text=fechaNacimiento
        etDireccion.setText(direccion)
        etTelefono.setText(telefono)
        //Genero (radio buttons)
        when (genero) {
            "F" -> rbF.isChecked = true
            "M" -> rbM.isChecked = true
            "Prefiero no decirlo" -> rbNoDecir.isChecked = true
        }
        //Socio (radio buttons)
        if (socio) {
            rbSocioSi.isChecked = true
        } else {
            rbSocioNo.isChecked = true
        }

        //Boton aceptar
        val botonAceptar: Button = findViewById(R.id.btnGuardar)

        botonAceptar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val dni = etDni.text.toString().toIntOrNull() ?: 0
            val fechaInscripcion = etFechaNac.text.toString()
            val direccion = etDireccion.text.toString()
            val telefono = etTelefono.text.toString()

            // Género
            val genero = when {
                rbF.isChecked -> "F"
                rbM.isChecked -> "M"
                else -> "Prefiero no decirlo"
            }

            // Socio
            val socio = rbSocioSi.isChecked

            // Pasar los datos al carnet
            val intent = Intent(this, CarnetActivity::class.java)
            intent.putExtra("nombreCompleto", nombreCompleto)
            intent.putExtra("id", id) // el id del cliente
            intent.putExtra("dni", dni)
            intent.putExtra("fechaInscripcion", fechaInscripcion)
            intent.putExtra("direccion", direccion)
            intent.putExtra("telefono", telefono)
            intent.putExtra("genero", genero)
            intent.putExtra("socio", socio)

            startActivity(intent)
        }
    }
}