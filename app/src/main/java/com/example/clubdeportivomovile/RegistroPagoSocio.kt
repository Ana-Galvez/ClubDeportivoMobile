package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.data.DBHelper

class RegistroPagoSocio : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: DBHelper
    private var listaSociosDB: List<Cliente> = listOf()

    private lateinit var spinnerCliente: Spinner
    private lateinit var spinnerCuotaPendiente: Spinner

    private lateinit var spinnerPago: Spinner
    private lateinit var spinnerCuotasTarjeta: Spinner
    private lateinit var etNumeroTarjeta: EditText
    private lateinit var montoEditText: EditText
    private lateinit var tituloCuotasTarjeta: TextView
    private lateinit var tituloNumeroTarjeta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago_socio)

        // Configurar header con botón atrás + hamburguesa
        setupDrawerMenu(R.id.drawerLayout) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")  //activo botones barra

        dbHelper = DBHelper(this)
        //clientes
        /*        data class Cliente(
            val id: Int,
            val nombre: String,
            val apellido: String,
            val socio: Boolean,
            val telefono: String,
            val ins: String,
            val direccion:String
        )*/

        /*        val clientes = listOf(
            Cliente(1, "María", "Golden", false, "09012345", "02/07/2024", "Calle Falsa 01"),
            Cliente(1, "María", "Silver", true, "523112345", "30/10/2024", "Callejón New"),
            Cliente(2, "Juan", "Chavo", true, "01112345", "15/04/2025", "Calle NoHay 10"),
            Cliente(3, "Lorena", "Sim", false, "04662345", "22/12/2024", "Calle S/N"),
            Cliente(3, "Armando", "Perez Gomez", false, "222662345", "13/12/2024", "Calle Nueva")
        )*/

        listaSociosDB = dbHelper.obtenerSociosClientes()
        spinnerCliente = findViewById(R.id.spinner_cliente_socio)
        val nombresClientesSpinner = mutableListOf("Seleccionar cliente...")
        nombresClientesSpinner.addAll(listaSociosDB.map { it.nombreCompleto })

        val adapterClientes = ArrayAdapter(
            this,
            R.layout.spinner_item_custom,
            nombresClientesSpinner
        )
        adapterClientes.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCliente.adapter = adapterClientes

        //Para no mostrar sugerencia de busqueda de nombres, ya que viene del listado de cliente
        val clienteSeleccionado = intent.getStringExtra("clienteSeleccionado")

        if (clienteSeleccionado != null) {
            val posicion = nombresClientesSpinner.indexOf(clienteSeleccionado)
            if (posicion >= 0) {
                spinnerCliente.setSelection(posicion)
                spinnerCliente.isEnabled = false
            }
        } else {
            spinnerCliente.setSelection(0) // Mostrar "Seleccionar cliente..."
        }

        // Cuota pendiente
        data class CuotaPendiente(
            val id: Int,
            val mesAnio: String,
            val monto: Int
        )

        val cuotasPendientes = listOf(
            CuotaPendiente(1, "Septiembre 2025", 35000),
            CuotaPendiente(2, "Julio 2025", 35000),
            CuotaPendiente(3, "Agosto 2025", 35000)
        )

        spinnerCuotaPendiente = findViewById(R.id.spinner_cuota_pendiente)
        montoEditText = findViewById(R.id.monto)

        val nombresCuotasPendientes = mutableListOf("Seleccionar cuota a pagar...")
        nombresCuotasPendientes.addAll(cuotasPendientes.map { it.mesAnio })

        val adapterCuotas = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            nombresCuotasPendientes
        ) {
            override fun isEnabled(position: Int): Boolean {
                // placeholder
                return position != 0
            }
        }


        adapterCuotas.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCuotaPendiente.adapter = adapterCuotas

        spinnerCuotaPendiente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                val cuotaPendienteSeleccionada =
                    cuotasPendientes[position - 1] // -1 por el placeholder
                montoEditText.setText(cuotaPendienteSeleccionada.monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerCuotaPendiente.setSelection(0)

        adapterCuotas.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCuotaPendiente.adapter = adapterCuotas

        spinnerCuotaPendiente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                val cuotaPendienteSeleccionada =
                    cuotasPendientes[position - 1] // -1 por el placeholder
                montoEditText.setText(cuotaPendienteSeleccionada.monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Mostrar el placeholder
        spinnerCuotaPendiente.setSelection(0)

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

        val nombresPago = mutableListOf("Seleccionar forma de pago...")
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
        adapterPago.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerPago.adapter = adapterPago
        spinnerPago.setSelection(0)

        // Cantidad de cuotas si se elige tarjeta de crédito
        val spinnerCuotasTarjeta: Spinner = findViewById(R.id.spinner_cuotas)
        val tituloCuotasTarjeta: TextView = findViewById(R.id.titulo_cuotas)
        etNumeroTarjeta = findViewById(R.id.num_tarjeta)
        val tituloNumeroTarjeta: TextView = findViewById(R.id.titulo_num_tarjeta)

        // Configuramos las opciones de cuotas
        val opcionesCuotasTarjeta = listOf("Seleccionar...", "3 cuotas", "6 cuotas")

        val adapterCuotasTarjeta = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            opcionesCuotasTarjeta
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }
        adapterCuotasTarjeta.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCuotasTarjeta.adapter = adapterCuotasTarjeta
        spinnerCuotasTarjeta.setSelection(0)

        spinnerCuotasTarjeta.visibility = View.GONE
        tituloCuotasTarjeta.visibility = View.GONE
        etNumeroTarjeta.visibility = View.GONE
        tituloNumeroTarjeta.visibility = View.GONE

        // Controlamos la visibilidad según forma de pago
        spinnerPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val seleccion = spinnerPago.selectedItem.toString()

                if (position==2) {
                    spinnerCuotasTarjeta.visibility = View.VISIBLE
                    tituloCuotasTarjeta.visibility = View.VISIBLE
                } else {
                    spinnerCuotasTarjeta.visibility = View.GONE
                    tituloCuotasTarjeta.visibility = View.GONE
                    etNumeroTarjeta.visibility = View.GONE
                    tituloNumeroTarjeta.visibility = View.GONE
                    spinnerCuotasTarjeta.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Número de tarjeta
        // Controlamos la visibilidad si se eligió la cantidad de cuotas
        spinnerCuotasTarjeta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                /*               val seleccion = spinnerCuotasTarjeta.selectedItem.toString()*/

                if (position > 0) {
                    etNumeroTarjeta.visibility = View.VISIBLE
                    tituloNumeroTarjeta.visibility = View.VISIBLE
                } else {
                    etNumeroTarjeta.visibility = View.GONE
                    tituloNumeroTarjeta.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Validaciones del formulario
        fun validarFormulario(
        ): Cliente? {

            val clientePosicion = spinnerCliente.selectedItemPosition
            val cuotaPosicion = spinnerCuotaPendiente.selectedItemPosition
            val pagoPosicion = spinnerPago.selectedItemPosition
            val cuotasTarjetaPosicion = spinnerCuotasTarjeta.selectedItemPosition

            val formaPago = spinnerPago.getItemAtPosition(pagoPosicion).toString()
            val numTarjeta = etNumeroTarjeta.text.toString().trim()
            /*            val nombreIngresado = clienteEditText.text.toString().trim()
            val cantCuotasTarjeta = spinnerCuotasTarjeta.selectedItemPosition !=0
            val cuotaPendienteSeleccionada = spinnerCuotaPendiente.selectedItemPosition != 0
            val clienteIngresado = nombreIngresado.isNotEmpty()

            // Buscar cliente en la base de datos
            val clienteEncontrado = clientes.find {
                "${it.nombre} ${it.apellido}".equals(nombreIngresado, ignoreCase = true)
            }*/

            if (clientePosicion == 0) {
                Toast.makeText(this, "Debe seleccionar un cliente.", Toast.LENGTH_SHORT).show()
                return null
            }

            /*            // Verificar si es no socio
            if (!clienteEncontrado.socio) {
                Toast.makeText(
                    this,
                    "El cliente es NO socio no se puede registrar el pago.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }*/
            /*            // campos vacíos
            if (!clienteIngresado && !cuotaPendienteSeleccionada && formaPago=="") {
                Toast.makeText(
                    this,
                    "Campos incompletos. Complete cliente, cuota y forma de pago.",
                    Toast.LENGTH_SHORT
                ).show()
                return null
            } else if (!clienteIngresado) {
                Toast.makeText(this, "Debe ingresar el nombre del cliente.", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (!cuotaPendienteSeleccionada) {
                Toast.makeText(this, "Debe seleccionar una de las cuotas.", Toast.LENGTH_SHORT).show()
                return false
            } else if (formaPago=="Seleccionar..."){
                Toast.makeText(this, "Debe seleccionar una forma de pago.", Toast.LENGTH_SHORT).show()
                return false
            } else if (formaPago=="Efectivo"){
                return true
            } else if (formaPago=="Tarjeta de crédito" && !cantCuotasTarjeta){
                Toast.makeText(this, "Debe seleccionar la cantidad de cuotas.", Toast.LENGTH_SHORT).show()
                return false
            } else if (numTarjeta==""){
                Toast.makeText(this, "Debe ingresar el número de tarjeta del socio.", Toast.LENGTH_SHORT).show()
                return false
            }*/

            if (cuotaPosicion == 0) {
                Toast.makeText(this, "Debe seleccionar una de las cuotas.", Toast.LENGTH_SHORT)
                    .show()
                return null
            }
            if (pagoPosicion == 0) {
                Toast.makeText(this, "Debe seleccionar una forma de pago.", Toast.LENGTH_SHORT)
                    .show()
                return null
            }

            if (formaPago == "Tarjeta de crédito") {
                if (cuotasTarjetaPosicion == 0) {
                    Toast.makeText(
                        this,
                        "Debe seleccionar la cantidad de cuotas.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return null
                }
                if (numTarjeta.isEmpty()) {
                    Toast.makeText(this, "Debe ingresar el número de tarjeta.", Toast.LENGTH_SHORT)
                        .show()
                    return null
                }
            }
            val clienteEncontrado = listaSociosDB[clientePosicion - 1]

            // Doble chequeo
            if (!clienteEncontrado.esSocio) {
                Toast.makeText(this, "El cliente no es socio.", Toast.LENGTH_LONG).show()
                return null
            }

            return clienteEncontrado
        }

            /*            // Validar que el nombre tenga solo letras y espacios
            val regexNombre = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+\$")
            if (!regexNombre.matches(nombreIngresado)) {
                Toast.makeText(
                    this,
                    "El nombre del cliente contiene caracteres inválidos.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            return true*/


        //Botones
        val botonAceptar: Button = findViewById(R.id.btnAceptarSocio)

        botonAceptar.setOnClickListener {
            val clienteEncontrado = validarFormulario()
            if (clienteEncontrado != null) {
                val cuotaPendienteSeleccionada = spinnerCuotaPendiente.selectedItem.toString()
                val formaPago = spinnerPago.selectedItem.toString()
                val numTarjeta = etNumeroTarjeta.text.toString().trim()
                val cantCuotasTarjeta = spinnerCuotasTarjeta.selectedItem.toString()
                val clienteNombre = spinnerCliente.selectedItem.toString()
                val monto = montoEditText.text.toString().trim()

                /*                val cuotaPendienteSeleccionada = spinnerCuotaPendiente.selectedItem.toString()
            val formaPago = spinnerPago.selectedItem.toString()
            val numTarjeta = etNumeroTarjeta.text.toString().trim()
            val cantCuotasTarjeta = spinnerCuotasTarjeta.selectedItem.toString()
            val clienteNombre = clienteEditText.text.toString().trim()
            val monto = montoEditText.text.toString().trim()
            //Para pasar los datos del cliente socio encontrado
            val clienteEncontrado = clientes.find {
                "${it.nombre} ${it.apellido}".equals(clienteNombre, ignoreCase = true)
            }!!*/
                //Paso la info al comprobante
                val intent = Intent(this, ReciboSocioActivity::class.java).apply {
                    putExtra("nombreCliente", clienteNombre)
                    putExtra("cuotaPendiente", cuotaPendienteSeleccionada)
                    putExtra("monto", monto)
                    putExtra("formaPago", formaPago)
                    putExtra("cantCuotasTarjeta", cantCuotasTarjeta)
                    putExtra("telefono", clienteEncontrado.Telefono)
                    putExtra("ins", clienteEncontrado.fechaInscripcionUI)
                    putExtra("direccion", clienteEncontrado.Direccion)
                }

                startActivity(intent)
            }
        }

        val botonLimpiar: Button = findViewById(R.id.btnLimpiarSocio)

        botonLimpiar.setOnClickListener {
            val rootLayout =
                findViewById<ViewGroup>(R.id.contentLayout) // el layout principal del form
            limpiarFormulario(rootLayout)
            spinnerCliente.setSelection(0)
            spinnerCuotaPendiente.setSelection(0)
            spinnerPago.setSelection(0)
        }
    }
}