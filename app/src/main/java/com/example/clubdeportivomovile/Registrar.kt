package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.limpiarFormulario
import android.app.DatePickerDialog
import android.util.Log
import com.example.clubdeportivomovile.data.DBHelper
import java.util.Calendar

class Registrar : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout

    private val db by lazy { DBHelper(this) }
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

    // Validar edad mínima (+18)
    private fun validarEdadMinima(fecha: String, edadMinima: Int): String? {
        return try {
            val partes = fecha.split("/")
            if (partes.size != 3) return "Fecha de nacimiento inválida"

            val dia = partes[0].toInt()
            val mes = partes[1].toInt() - 1
            val anio = partes[2].toInt()

            val fechaNac = Calendar.getInstance().apply { set(anio, mes, dia) }
            val hoy = Calendar.getInstance()

            var edad = hoy.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR)

            if (hoy.get(Calendar.MONTH) < fechaNac.get(Calendar.MONTH) ||
                (hoy.get(Calendar.MONTH) == fechaNac.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) < fechaNac.get(Calendar.DAY_OF_MONTH))
            ) {
                edad--
            }

            if (edad < edadMinima) "Debe ser mayor de $edadMinima años" else null

        } catch (e: Exception) {
            "Fecha de nacimiento inválida"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout) ///* menu ---va el id como parametro
        setupBottomBar("nuevo")  // barra

        // Referencias a los campos
        val etNombre: EditText = findViewById(R.id.etNombre)
        val etApellido: EditText = findViewById(R.id.etApellido)

        val rowFechaNac = findViewById<LinearLayout>(R.id.rowFechaNac)
        val tvFechaNac: TextView = findViewById(R.id.tvFechaNac)

        val etDni: EditText = findViewById(R.id.etDni)
        val etDireccion: EditText = findViewById(R.id.etDireccion)
        val etTelefono: EditText = findViewById(R.id.etTelefono)
        val rbg: RadioGroup = findViewById(R.id.rbg)
        val rgSocio: RadioGroup = findViewById(R.id.rgSocio)

        val botonAceptar: Button = findViewById(R.id.btnGuardar)
        val botonLimpiar: Button = findViewById(R.id.btnLimpiar)

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

        botonAceptar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val fechaNac = tvFechaNac.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val generoSeleccionado = rbg.checkedRadioButtonId
            val socioSeleccionado = rgSocio.checkedRadioButtonId

            val errorFecha = if (fechaNac.isNotEmpty() && fechaNac != "DD/MM/YYYY")
                validarFechaNacNoFutura(fechaNac)
            else null

            val errorEdad = validarEdadMinima(fechaNac, 18)

            when {
                nombre.isEmpty() -> Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT).show()
                apellido.isEmpty() -> Toast.makeText(this, "Ingrese el apellido", Toast.LENGTH_SHORT).show()
                fechaNac.isEmpty() || fechaNac == "DD/MM/YYYY" -> Toast.makeText(this, "Seleccione la fecha de nacimiento", Toast.LENGTH_SHORT).show()
                errorFecha != null -> Toast.makeText(this, errorFecha, Toast.LENGTH_SHORT).show()
                errorEdad != null -> Toast.makeText(this, errorEdad, Toast.LENGTH_SHORT).show()
                dni.isEmpty() -> Toast.makeText(this, "Ingrese el DNI", Toast.LENGTH_SHORT).show()
                dni.length < 7 -> Toast.makeText(this, "El DNI debe tener al menos 7 dígitos", Toast.LENGTH_SHORT).show()
                direccion.isEmpty() -> Toast.makeText(this, "Ingrese la dirección", Toast.LENGTH_SHORT).show()
                telefono.isEmpty() -> Toast.makeText(this, "Ingrese el teléfono", Toast.LENGTH_SHORT).show()
                !telefono.matches(Regex("^[0-9]{8,15}$")) -> Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show()
                generoSeleccionado == -1 -> Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show()
                socioSeleccionado == -1 -> Toast.makeText(this, "Seleccione tipo de socio", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this, "Datos validados correctamente", Toast.LENGTH_SHORT).show()

                    val nombreCompleto = "$nombre $apellido"
                    val dniInt = dni.toIntOrNull() ?: 0
                    val esSocio = if (rgSocio.checkedRadioButtonId == R.id.rbSocioSi) 1 else 0
                    val rbGenero = findViewById<RadioButton>(generoSeleccionado)
                    val generoTexto = rbGenero.text.toString()
                    val calendario = Calendar.getInstance()
                    val fechaInscripcion = "%02d/%02d/%04d".format(
                        calendario.get(Calendar.DAY_OF_MONTH),
                        calendario.get(Calendar.MONTH) + 1,
                        calendario.get(Calendar.YEAR)
                    )

                    val db = DBHelper(this)
                    db.insertarCliente(
                        nombre,
                        apellido,
                        fechaNac,
                        dniInt,
                        generoTexto,
                        direccion,
                        telefono,
                        fechaInscripcion,
                        true,
                        socioSeleccionado == R.id.rbSocioSi
                    )

                    val idClienteReal = db.obtenerIdPorDni(dniInt)

                    val intent = Intent(this, CarnetActivity::class.java).apply {
                        putExtra("nombreCompleto", "$nombre $apellido")
                        putExtra("id", idClienteReal)  // ID REAL, NO RANDOM
                        putExtra("dni", dniInt)
                        putExtra("socio", if (socioSeleccionado == R.id.rbSocioSi) 1 else 0)
                        putExtra("direccion", direccion)
                        putExtra("telefono", telefono)
                        putExtra("fechaInscripcion", fechaInscripcion)
                    }
                    Log.d("Registro", "ID idClienteReal $idClienteReal")
                    startActivity(intent)
                }
            }
        }

        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.contentLayout)
            limpiarFormulario(rootLayout)
            etNombre.requestFocus()
        }
    }
}
