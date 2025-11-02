package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.limpiarFormulario

class PagoNoSocioActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pago_no_socios)
        setupDrawerMenu(R.id.drawer_layout_no_socio) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")

        //clientes
        data class Cliente(
            val id: Int,
            val nombre: String,
            val apellido: String,
            val socio: Boolean,
            val telefono: String,
            val ins: String,
            val direccion:String
        )

        val clientes = listOf(
            Cliente(1, "María", "Golden", false, "09012345", "02/07/2024", "Calle Falsa 01"),
            Cliente(1, "María", "Silver", false, "523112345", "30/10/2024", "Callejón New"),
            Cliente(2, "Juan", "Chavo", true, "01112345", "15/04/2025", "Calle NoHay 10"),
            Cliente(3, "Lorena", "Sim", false, "04662345", "22/12/2024", "Calle S/N"),
            Cliente(3, "Armando", "Perez Gomez", false, "222662345", "13/12/2024", "Calle Nueva")
        )

        val clienteEditText: AutoCompleteTextView= findViewById(R.id.formNoSocioCliente)

        //Buscador
        val nombresClientes = clientes.map { "${it.nombre} ${it.apellido}" }

        val adapterClientes = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresClientes
        )

        // Para mostrar sugerencia de busqueda de nombres
        clienteEditText.setAdapter(adapterClientes)

        //Para no mostrar sugerencia de busqueda de nombres, ya que viene del listado de cliente
        val clienteSeleccionado = intent.getStringExtra("clienteSeleccionado")

        if (clienteSeleccionado != null) {
            // Mostrar nombre directamente
            clienteEditText.setText(clienteSeleccionado)
            clienteEditText.isEnabled = false
            //Saca icono de busqueda
            clienteEditText.setCompoundDrawables(null, null, null, null)
        }

        //actividades
        data class Actividad(
            val id: Int,
            val nombre: String,
            val precio: Int,
            val diaSemana: String,
            val hora: String
        )

        val actividades = listOf(
            Actividad(1, "Yoga", 15000, "Lunes", "11:00 hs"),
            Actividad(2, "Tenis", 18000, "Martes", "14:00 hs"),
            Actividad(3, "Natación", 20000, "Miércoles", "16:30 hs")
        )

        val spinnerActividad: Spinner = findViewById(R.id.spinner_pago_no_socio)
        val horarioEditText: EditText = findViewById(R.id.formNoSocioHorario)
        val montoEditText: EditText = findViewById(R.id.formNoSocioMonto)

        val nombresActividades = mutableListOf("Seleccionar...")
        nombresActividades.addAll(actividades.map { it.nombre })

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            nombresActividades
        ) {
            override fun isEnabled(position: Int): Boolean {
                // placeholder
                return position != 0
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActividad.adapter = adapter

        spinnerActividad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    horarioEditText.text.clear()
                    montoEditText.text.clear()
                    return
                }

                val actividadSeleccionada = actividades[position - 1] // -1 por el placeholder
                horarioEditText.setText(actividadSeleccionada.hora)
                montoEditText.setText(actividadSeleccionada.precio.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerActividad.setSelection(0)

        adapter.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerActividad.adapter = adapter

        spinnerActividad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    horarioEditText.text.clear()
                    montoEditText.text.clear()
                    return
                }

                val actividadSeleccionada = actividades[position - 1] // -1 por el placeholder
                horarioEditText.setText(actividadSeleccionada.hora)
                montoEditText.setText(actividadSeleccionada.precio.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Mostrar el placeholder
        spinnerActividad.setSelection(0)

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout_no_socio)
        setupHeader(drawerLayout)

        //Validaciones del formulario
        fun validarFormulario(
            spinnerActividad: Spinner,
            clienteEditText: EditText,
            clientes: List<Cliente>
        ): Boolean {
            val nombreIngresado = clienteEditText.text.toString().trim()

            val actividadSeleccionada = spinnerActividad.selectedItemPosition != 0
            val clienteIngresado = nombreIngresado.isNotEmpty()

            // campos vacíos
            if (!clienteIngresado && !actividadSeleccionada) {
                Toast.makeText(
                    this,
                    "Campos incompletos. Complete cliente y actividad.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!clienteIngresado) {
                Toast.makeText(this, "Debe ingresar el nombre del cliente.", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (!actividadSeleccionada) {
                Toast.makeText(this, "Debe seleccionar una actividad.", Toast.LENGTH_SHORT).show()
                return false
            }

            // Validar que el nombre tenga solo letras y espacios
            val regexNombre = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+\$")
            if (!regexNombre.matches(nombreIngresado)) {
                Toast.makeText(
                    this,
                    "El nombre del cliente contiene caracteres inválidos.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            // Buscar cliente en la base de datos
            val clienteEncontrado = clientes.find {
                "${it.nombre} ${it.apellido}".equals(nombreIngresado, ignoreCase = true)
            }

            if (clienteEncontrado == null) {
                Toast.makeText(this, "El cliente no existe.", Toast.LENGTH_SHORT).show()
                return false
            }

            // Verificar si es no socio
            if (clienteEncontrado.socio) {
                Toast.makeText(
                    this,
                    "El cliente es socio no se puede registrar el pago.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            return true
        }

        //Botones
        val botonAceptar: Button = findViewById(R.id.BotonAceptarPagoNoSocio)

        botonAceptar.setOnClickListener {
            if (validarFormulario(spinnerActividad, clienteEditText, clientes = clientes)) {
                val actividadSeleccionada = spinnerActividad.selectedItem.toString()
                val clienteNombre = clienteEditText.text.toString().trim()
                val horario = horarioEditText.text.toString().trim()
                val monto = montoEditText.text.toString().trim()
                //Para pasar los datos del cliente no socio encontrado
                val clienteEncontrado = clientes.find {
                    "${it.nombre} ${it.apellido}".equals(clienteNombre, ignoreCase = true)
                }!!
                //Paso la info al comprobante
                val intent = Intent(this, ReciboPagoNoSocioActivity::class.java).apply {
                    putExtra("nombreCliente", clienteNombre)
                    putExtra("actividad", actividadSeleccionada)
                    putExtra("horario", horario)
                    putExtra("monto", monto)
                    putExtra("telefono", clienteEncontrado.telefono)
                    putExtra("ins", clienteEncontrado.ins)
                    putExtra("direccion", clienteEncontrado.direccion)
                }

                startActivity(intent)
            }
        }

        val botonLimpiar: Button = findViewById(R.id.BotonLimpiarPagoNoSocio)

        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.content_Layout)
            limpiarFormulario(rootLayout)

            // foco al primer campo
            clienteEditText.requestFocus()
        }

        //activo botones barra
        setupBottomBar("pagos")
    }
}

