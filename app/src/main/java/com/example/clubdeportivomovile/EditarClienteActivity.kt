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
import com.example.clubdeportivomovile.data.DBHelper
import java.util.Calendar
import kotlin.text.isEmpty

class EditarClienteActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var botonAceptar: Button

    // Variables para almacenar valores originales
    private var nombreOriginal = ""
    private var apellidoOriginal = ""
    private var dniOriginal = 0
    private var fechaNacOriginal = ""
    private var direccionOriginal = ""
    private var telefonoOriginal = ""
    private var generoOriginal = ""
    private var socioOriginal = false

    private fun validarFechaNacNoFutura(fecha: String): String? {
        return try {
            val parts = fecha.split("/")
            if (parts.size != 3) return "Fecha de nacimiento inválida"

            val dia = parts[0].toInt()
            val mes = parts[1].toInt() - 1
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

        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawerLayout)
        setupBottomBar("")

        // Obtener referencias a los campos
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etDni = findViewById<EditText>(R.id.etDni)
        val tvFechaNac: TextView = findViewById(R.id.tvFechaNac)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val rbF = findViewById<RadioButton>(R.id.rbF)
        val rbM = findViewById<RadioButton>(R.id.rbM)
        val rbNoDecir = findViewById<RadioButton>(R.id.rbNoDecir)
        val rbSocioSi = findViewById<RadioButton>(R.id.rbSocioSi)
        val rbSocioNo = findViewById<RadioButton>(R.id.rbSocioNo)
        val rbg: RadioGroup = findViewById(R.id.rbg)
        val rgSocio: RadioGroup = findViewById(R.id.rgSocio)

        botonAceptar = findViewById(R.id.btnGuardar)

        // Recibir datos originales del cliente
        val id = intent.getIntExtra("id", -1)
        nombreOriginal = intent.getStringExtra("nombre") ?: ""
        apellidoOriginal = intent.getStringExtra("apellido") ?: ""
        dniOriginal = intent.getIntExtra("dni", 0)
        fechaNacOriginal = intent.getStringExtra("fechaNacimiento") ?: ""
        direccionOriginal = intent.getStringExtra("direccion") ?: ""
        telefonoOriginal = intent.getStringExtra("telefono") ?: ""
        generoOriginal = intent.getStringExtra("genero") ?: ""
        socioOriginal = intent.getIntExtra("socio", 0) == 1
        val fechaInscripcion = intent.getStringExtra("fechaInscripcion")?: ""

        // Cargar datos en el formulario
        etNombre.setText(nombreOriginal)
        etApellido.setText(apellidoOriginal)
        etDni.setText(dniOriginal.toString())
        tvFechaNac.text = fechaNacOriginal
        etDireccion.setText(direccionOriginal)
        etTelefono.setText(telefonoOriginal)

        when (generoOriginal) {
            "F" -> rbF.isChecked = true
            "M" -> rbM.isChecked = true
            "Prefiero no decirlo" -> rbNoDecir.isChecked = true
        }

        rgSocio.clearCheck()
        if (socioOriginal) {
            rbSocioSi.isChecked = true
        } else {
            rbSocioNo.isChecked = true
        }

        // Editar fecha de nacimiento
        val rowFechaNac = findViewById<LinearLayout>(R.id.rowFechaNac)
        rowFechaNac.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fechaSeleccionada = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    tvFechaNac.text = fechaSeleccionada
                },
                anio, mes, dia
            )
            datePicker.show()
        }

        // Botón Limpiar
        val botonLimpiar: Button = findViewById(R.id.btnLimpiar)
        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.contentLayout)
            limpiarFormulario(rootLayout)
            etNombre.requestFocus()
        }

        // Botón Aceptar
        botonAceptar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val nombreCompletoEditado = "$nombre $apellido"
            val dniString = etDni.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val fechaNac = tvFechaNac.text.toString().trim()
            val generoSeleccionado = rbg.checkedRadioButtonId
            val socioSeleccionado = rgSocio.checkedRadioButtonId

            // Verificar si hubo cambios
            val generoActual = when {
                rbF.isChecked -> "F"
                rbM.isChecked -> "M"
                rbNoDecir.isChecked -> "Prefiero no decirlo"
                else -> ""
            }

            val socioActual = rbSocioSi.isChecked

            val hayCambios = nombre != nombreOriginal ||
                    apellido != apellidoOriginal ||
                    dniString != dniOriginal.toString() ||
                    fechaNac != fechaNacOriginal ||
                    direccion != direccionOriginal ||
                    telefono != telefonoOriginal ||
                    generoActual != generoOriginal ||
                    socioActual != socioOriginal

            if (!hayCambios) {
                Toast.makeText(this, "No hay cambios para guardar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val errorFecha = if (fechaNac.isNotEmpty() && fechaNac != "DD/MM/YYYY")
                validarFechaNacNoFutura(fechaNac)
            else null
            //Validación
            when {
                nombre.isEmpty() -> Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT).show()
                apellido.isEmpty() -> Toast.makeText(this, "Ingrese el apellido", Toast.LENGTH_SHORT).show()
                fechaNac.isEmpty() || fechaNac == "DD/MM/YYYY" -> Toast.makeText(this, "Seleccione la fecha de nacimiento", Toast.LENGTH_SHORT).show()
                errorFecha != null -> Toast.makeText(this, errorFecha, Toast.LENGTH_SHORT).show()
                dniString.isEmpty() -> Toast.makeText(this, "Ingrese el DNI", Toast.LENGTH_SHORT).show()
                dniString.length < 7 -> Toast.makeText(this, "El DNI debe tener al menos 7 dígitos", Toast.LENGTH_SHORT).show()
                direccion.isEmpty() -> Toast.makeText(this, "Ingrese la dirección", Toast.LENGTH_SHORT).show()
                telefono.isEmpty() -> Toast.makeText(this, "Ingrese el teléfono", Toast.LENGTH_SHORT).show()
                !telefono.matches(Regex("^[0-9]{8,15}$")) -> Toast.makeText(this, "Teléfono inválido", Toast.LENGTH_SHORT).show()
                generoSeleccionado == -1 -> Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show()
                socioSeleccionado == -1 -> Toast.makeText(this, "Seleccione tipo de socio", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(this, "Datos validados correctamente", Toast.LENGTH_SHORT).show()
                    //Envio datos a la DB
                    val dniEditado = dniString.toIntOrNull()!!
                    val socioEditado = if (rbSocioSi.isChecked) 1 else 0
                    val aptoFisico = 1
                    val dbHelper = DBHelper(this)
                    val actualizado = dbHelper.actualizarCliente(
                        idCliente = id,
                        nombre = nombre,
                        apellido = apellido,
                        fechaNac = fechaNac,
                        dni = dniEditado,
                        genero = generoActual,
                        direccion = direccion,
                        telefono = telefono,
                        fechaInsc = fechaInscripcion,
                        aptoFisico = aptoFisico,
                        socio = socioEditado
                    )
                    if (actualizado) {
                        Toast.makeText(this, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                        // Paso los datos al carnet
                        val intent = Intent(this, CarnetActivity::class.java)
                        intent.putExtra("nombreCompleto", nombreCompletoEditado)
                        intent.putExtra("id", id)
                        intent.putExtra("dni", dniEditado)
                        intent.putExtra("fechaInscripcion", fechaInscripcion)
                        intent.putExtra("direccion", direccion)
                        intent.putExtra("telefono", telefono)
                        intent.putExtra("genero", generoActual)
                        intent.putExtra("socio", socioEditado)

                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show()
                    }
                }

                }
            }
        }
    }