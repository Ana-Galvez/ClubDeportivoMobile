package com.example.clubdeportivomovile


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.limpiarFormulario
import java.util.Calendar
import kotlin.text.isEmpty

class EditarClienteActivity : BaseActivity() { //cambiamos de quien hereda asi tiene la fc de la barra
    private lateinit var drawerLayout: DrawerLayout

    private fun validarFechaNacNoFutura(fecha: String): String? {
        // Devuelve un String con el mensaje de error, o null si está OK
        return try {
            val parts = fecha.split("/")
            if (parts.size != 3) return "Fecha de nacimiento inválida"

            val dia = parts[0].toInt()
            val mes = parts[1].toInt() - 1   // Calendar: meses 0..11
            val anio = parts[2].toInt()

            val calNac = java.util.Calendar.getInstance().apply {
                set(anio, mes, dia, 0, 0, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val hoy = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }

            if (calNac.after(hoy)) "La fecha de nacimiento no puede ser futura" else null
        } catch (e: Exception) {
            "Fecha de nacimiento inválida"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)
        drawerLayout = findViewById(R.id.drawerLayout)

        // header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) /// menu ---va el id como parametro
        setupBottomBar("")  //barra, cadena vacia para q no resalte ningun boton de la barra
        //Editar fecha de nacimiento
        val rowFechaNac = findViewById<LinearLayout>(R.id.rowFechaNac)
        val tvFechaNac: TextView = findViewById(R.id.tvFechaNac)

        rowFechaNac.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Cuando el usuario selecciona la fecha:
                    val fechaSeleccionada = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    tvFechaNac.text = fechaSeleccionada
                },
                anio, mes, dia
            )
            datePicker.show()
        }

        //Botones Limpiar
        val botonLimpiar: Button = findViewById(R.id.btnLimpiar)
        val etNombre = findViewById<EditText>(R.id.etNombre)

        botonLimpiar.setOnClickListener {
            val rootLayout =
                findViewById<ViewGroup>(R.id.contentLayout) // el layout principal del form
            limpiarFormulario(rootLayout)

            // foco al primer campo
            etNombre.requestFocus()
        }

        //Formulario
        //Recibo datos del cliente
        val id = intent.getIntExtra("id", -1)
        //Formulario editar necesita nombre y apellido separado
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""
        val dni = intent.getIntExtra("dni", 0)
        val fechaNacimientoEditada = intent.getStringExtra("fechaNacimiento")
        val direccion = intent.getStringExtra("direccion")
        val telefono = intent.getStringExtra("telefono")
        val genero = intent.getStringExtra("genero")
        val fechaInscripcion = intent.getStringExtra("fechaInscripcion")

        //Agrego los datos al formulario
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etDni = findViewById<EditText>(R.id.etDni)
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
        tvFechaNac.text = fechaNacimientoEditada
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
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            //Envio el nombre completo editado si se modifico
            val nombreCompletoEditado = "$nombre $apellido"
            val dniString = etDni.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            //Este dato no se edita
            val fechaInscripcion = fechaInscripcion
            val fechaNac = tvFechaNac.text.toString().trim()
            val rbg: RadioGroup = findViewById(R.id.rbg)
            val rgSocio: RadioGroup = findViewById(R.id.rgSocio)
            val generoSeleccionado = rbg.checkedRadioButtonId
            val socioSeleccionado = rgSocio.checkedRadioButtonId

            val errorFecha = if (fechaNac.isNotEmpty() && fechaNac != "DD/MM/YYYY")
                validarFechaNacNoFutura(fechaNac)
            else null

            when {
                nombre.isEmpty() -> Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT)
                    .show()

                apellido.isEmpty() -> Toast.makeText(
                    this,
                    "Ingrese el apellido",
                    Toast.LENGTH_SHORT
                ).show()

                fechaNac.isEmpty() || fechaNac == "DD/MM/YYYY" -> Toast.makeText(
                    this,
                    "Seleccione la fecha de nacimiento",
                    Toast.LENGTH_SHORT
                ).show()

                errorFecha != null -> Toast.makeText(this, errorFecha, Toast.LENGTH_SHORT).show()
                dniString.isEmpty() -> {
                    Toast.makeText(this, "Ingrese el DNI", Toast.LENGTH_SHORT).show()
                }
                dniString.length < 7 -> {
                    Toast.makeText(this, "El DNI debe tener al menos 7 dígitos", Toast.LENGTH_SHORT).show()
                }

                direccion.isEmpty() -> Toast.makeText(
                    this,
                    "Ingrese la dirección",
                    Toast.LENGTH_SHORT
                ).show()

                telefono.isEmpty() -> Toast.makeText(
                    this,
                    "Ingrese el teléfono",
                    Toast.LENGTH_SHORT
                ).show()

                !telefono.matches(Regex("^[0-9]{8,15}$")) -> Toast.makeText(
                    this,
                    "Teléfono inválido",
                    Toast.LENGTH_SHORT
                ).show()

                generoSeleccionado == -1 -> Toast.makeText(
                    this,
                    "Seleccione un género",
                    Toast.LENGTH_SHORT
                ).show()

                socioSeleccionado == -1 -> Toast.makeText(
                    this,
                    "Seleccione tipo de socio",
                    Toast.LENGTH_SHORT
                ).show()

                else -> {
                    Toast.makeText(this, "Datos validados correctamente", Toast.LENGTH_SHORT).show()
                    // Socio
                    val socio = rbSocioSi.isChecked
                    val dniEditado = dniString.toIntOrNull()!!
                    // Pasar los datos al carnet
                    val intent = Intent(this, CarnetActivity::class.java)
                    intent.putExtra("nombreCompleto", nombreCompletoEditado)
                    intent.putExtra("id", id)
                    intent.putExtra("dni", dniEditado)
                    intent.putExtra("fechaInscripcion", fechaInscripcion)
                    intent.putExtra("direccion", direccion)
                    intent.putExtra("telefono", telefono)
                    intent.putExtra("genero", generoSeleccionado)
                    intent.putExtra("socio", socioSeleccionado)

                    startActivity(intent)
                }


            }

        }
    }

}
