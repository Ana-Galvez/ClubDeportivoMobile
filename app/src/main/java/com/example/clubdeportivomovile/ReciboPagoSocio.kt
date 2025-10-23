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
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import limpiarFormulario

class ReciboPagoSocio : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_pago_socio)

        // Configurar header con botón atrás + hamburguesa
        setupDrawerMenu(R.id.drawerLayout) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")  //activo botones barra

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
            Cliente(1, "María", "Silver", true, "523112345", "30/10/2024", "Callejón New"),
            Cliente(2, "Juan", "Chavo", true, "01112345", "15/04/2025", "Calle NoHay 10"),
            Cliente(3, "Lorena", "Sim", false, "04662345", "22/12/2024", "Calle S/N"),
            Cliente(3, "Armando", "Perez Gomez", false, "222662345", "13/12/2024", "Calle Nueva")
        )

        val clienteEditText: AutoCompleteTextView= findViewById(R.id.cliente)

        val nombresClientes = clientes.map { "${it.nombre} ${it.apellido}" }

        val adapterClientes = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresClientes
        )

        // Para mostrar sugerencia de busqueda de nombres
        clienteEditText.setAdapter(adapterClientes)


        // Cuota pendiente
        data class Cuota(
            val id: Int,
            val mesAnio: String,
            val monto: Int
        )

        val cuotas = listOf(
            Cuota(1, "Septiembre 2025", 35000),
            Cuota(2, "Julio 2025", 35000),
            Cuota(3, "Agosto 2025", 35000)
        )

        val spinnerCuota: Spinner = findViewById(R.id.spinner_cuota_pendiente)
        val montoEditText: EditText = findViewById(R.id.monto)

        val nombresCuotas = mutableListOf("Seleccionar...")
        nombresCuotas.addAll(cuotas.map { it.mesAnio })

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            nombresCuotas
        ) {
            override fun isEnabled(position: Int): Boolean {
                // placeholder
                return position != 0
            }
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCuota.adapter = adapter

        spinnerCuota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    montoEditText.text.clear()
                    return
                }

                val cuotaSeleccionada = cuotas[position - 1] // -1 por el placeholder
                montoEditText.setText(cuotaSeleccionada.monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCuota.setSelection(0)

        adapter.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCuota.adapter = adapter

        spinnerCuota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    montoEditText.text.clear()
                    return
                }

                val cuotaSeleccionada = cuotas[position - 1] // -1 por el placeholder
                montoEditText.setText(cuotaSeleccionada.monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Mostrar el placeholder
        spinnerCuota.setSelection(0)

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        setupHeader(drawerLayout)


        // Forma de pago
        data class FormaPago(
            val id: Int,
            val medioPago: String,
        )

        val formasPagos = listOf(
            FormaPago(1, "Efectivo"),
            FormaPago(2, "Tarjeta de crédito"),
        )

        val spinnerPago: Spinner = findViewById(R.id.spinner_pago)

        val nombresPago = mutableListOf("Seleccionar...")
        nombresPago.addAll(formasPagos.map { it.medioPago })

        // Crear el adapter para mostrar las opciones en el Spinner
        val adapterPago = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            nombresPago
        ) {
            override fun isEnabled(position: Int): Boolean {
                // deshabilita el placeholder
                return position != 0
            }
        }

        // Vista desplegable
        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asignar el adapter al Spinner
        spinnerPago.adapter = adapterPago
        // Mostrar el placeholder por defecto
        spinnerPago.setSelection(0)


        //Validaciones del formulario
        fun validarFormulario(
            spinnerCuota: Spinner,
            spinnerFormaPago: Spinner,
            clienteEditText: EditText,
            clientes: List<Cliente>
        ): Boolean {
            val nombreIngresado = clienteEditText.text.toString().trim()
            val formaPago = spinnerFormaPago.selectedItemPosition !=0
            val cuotaSeleccionada = spinnerCuota.selectedItemPosition != 0
            val clienteIngresado = nombreIngresado.isNotEmpty()

            // campos vacíos
            if (!clienteIngresado && !cuotaSeleccionada && !formaPago) {
                Toast.makeText(
                    this,
                    "Campos incompletos. Complete cliente, cuota y forma de pago.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!clienteIngresado) {
                Toast.makeText(this, "Debe ingresar el nombre del cliente.", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (!cuotaSeleccionada) {
                Toast.makeText(this, "Debe seleccionar una cuota.", Toast.LENGTH_SHORT).show()
                return false
            } else if (!formaPago){
                Toast.makeText(this, "Debe seleccionar una forma de pago.", Toast.LENGTH_SHORT).show()
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
            if (!clienteEncontrado.socio) {
                Toast.makeText(
                    this,
                    "El cliente es NO socio no se puede registrar el pago.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            return true
        }

        //Botones
        val botonAceptar: Button = findViewById(R.id.btnAceptarSocio)

        botonAceptar.setOnClickListener {
            if (validarFormulario(spinnerCuota, spinnerPago,clienteEditText, clientes = clientes)) {
                val cuotaSeleccionada = spinnerCuota.selectedItem.toString()
                val formaPago = spinnerPago.selectedItem.toString()
                val clienteNombre = clienteEditText.text.toString().trim()
                val monto = montoEditText.text.toString().trim()
                //Para pasar los datos del cliente socio encontrado
                val clienteEncontrado = clientes.find {
                    "${it.nombre} ${it.apellido}".equals(clienteNombre, ignoreCase = true)
                }!!
                //Paso la info al comprobante
                val intent = Intent(this, ReciboSocioActivity::class.java).apply {
                    putExtra("nombreCliente", clienteNombre)
                    putExtra("cuota", cuotaSeleccionada)
                    putExtra("monto", monto)
                    putExtra("formaPago",formaPago)
                    putExtra("telefono", clienteEncontrado.telefono)
                    putExtra("ins", clienteEncontrado.ins)
                    putExtra("direccion", clienteEncontrado.direccion)
                }

                startActivity(intent)
            }
        }

        val botonLimpiar: Button = findViewById(R.id.btnLimpiarSocio)
        val etNombre: EditText = findViewById(R.id.cliente)

        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.contentLayout) // el layout principal del form
            limpiarFormulario(rootLayout)

            // foco al primer campo
            etNombre.requestFocus()
        }
    }
}