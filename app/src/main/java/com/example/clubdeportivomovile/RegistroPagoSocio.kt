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
import androidx.appcompat.app.AlertDialog
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroPagoSocio : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: DBHelper
    private var listaSociosDB: List<Cliente> = listOf()
    private var listaCuotasPendientesDB: List<Cuotas> = listOf()

    private lateinit var spinnerCliente: Spinner
    private lateinit var spinnerCuotaPendiente: Spinner
    private lateinit var etNumeroTarjeta: EditText
    private lateinit var montoEditText: EditText
    private var vieneConClienteSeleccionado = false
    private var nombreClienteBloqueado: String? = null

    private lateinit var adapterCuotas: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago_socio)

        // Configurar header con botón atrás + hamburguesa
        setupDrawerMenu(R.id.drawerLayout) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")  //activo botones barra

        dbHelper = DBHelper(this)

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
            vieneConClienteSeleccionado = true
            nombreClienteBloqueado = clienteSeleccionado

            val posicion = nombresClientesSpinner.indexOf(clienteSeleccionado)
            if (posicion >= 0) {
                spinnerCliente.setSelection(posicion)
                spinnerCliente.isEnabled = false

            }
        } else {
            spinnerCliente.setSelection(0)
            spinnerCliente.isEnabled = true
        }

        spinnerCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    listaCuotasPendientesDB = listOf()
                    actualizarSpinnerCuotas(listOf())
                    montoEditText.text.clear()
                } else {
                    // Un cliente fue seleccionado, buscamos sus cuotas
                    val cliente = listaSociosDB[position - 1]
                    // Llamamos a la nueva función de DBHelper
                    listaCuotasPendientesDB = dbHelper.obtenerCuotasPendientes(cliente.id)
                    actualizarSpinnerCuotas(listaCuotasPendientesDB)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Limpiar si no hay nada seleccionado
                listaCuotasPendientesDB = listOf()
                actualizarSpinnerCuotas(listOf())
                montoEditText.text.clear()
            }
        }

        spinnerCuotaPendiente = findViewById(R.id.spinner_cuota_pendiente)
        montoEditText = findViewById(R.id.monto)

        adapterCuotas = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            mutableListOf("Seleccionar cuota a pagar...")
        ) {
            override fun isEnabled(position: Int): Boolean {
                // placeholder
                return position != 0
            }
        }


        adapterCuotas.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCuotaPendiente.adapter = adapterCuotas

        spinnerCuotaPendiente.isEnabled = false

        spinnerCuotaPendiente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0 || listaCuotasPendientesDB.isEmpty()) {
                    montoEditText.text.clear()
                    return
                }

                val cuotaSeleccionada =
                    listaCuotasPendientesDB[position - 1] // -1 por el placeholder
                montoEditText.setText(cuotaSeleccionada.Monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

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
        fun validarFormulario(): Cliente? {
            val clientePosicion = spinnerCliente.selectedItemPosition
            val cuotaPosicion = spinnerCuotaPendiente.selectedItemPosition
            val pagoPosicion = spinnerPago.selectedItemPosition
            val cuotasTarjetaPosicion = spinnerCuotasTarjeta.selectedItemPosition
            val formaPago = spinnerPago.getItemAtPosition(pagoPosicion).toString()
            val numTarjeta = etNumeroTarjeta.text.toString().trim()

            if (clientePosicion == 0) {
                Toast.makeText(this, "Debe seleccionar un cliente.", Toast.LENGTH_SHORT).show()
                return null
            }

            // Revisar si hay cuotas para pagar
            if (listaCuotasPendientesDB.isEmpty()) {
                Toast.makeText(this, "El cliente no tiene cuotas pendientes.", Toast.LENGTH_SHORT).show()
                return null
            }

            if (cuotaPosicion == 0) {
                Toast.makeText(this, "Debe seleccionar una de las cuotas.", Toast.LENGTH_SHORT).show()
                return null
            }
            if (pagoPosicion == 0) {
                Toast.makeText(this, "Debe seleccionar una forma de pago.", Toast.LENGTH_SHORT).show()
                return null
            }
            if (formaPago == "Tarjeta de crédito") {
                if (cuotasTarjetaPosicion == 0) {
                    Toast.makeText(this, "Debe seleccionar la cantidad de cuotas.", Toast.LENGTH_SHORT).show()
                    return null
                }
                if (numTarjeta.isEmpty()) {
                    Toast.makeText(this, "Debe ingresar los 16 números de la tarjeta .", Toast.LENGTH_SHORT).show()
                    return null
                }
                if (numTarjeta.length < 16) {
                    Toast.makeText(this, "Debe ingresar los 16 números de la tarjeta.", Toast.LENGTH_SHORT).show()
                    return null
                }
            }
            val clienteEncontrado = listaSociosDB[clientePosicion - 1]
            if (!clienteEncontrado.esSocio) {
                Toast.makeText(this, "El cliente no es socio.", Toast.LENGTH_LONG).show()
                return null
            }
            return clienteEncontrado
        }

        //Botones
        val botonAceptar: Button = findViewById(R.id.btnAceptarSocio)

        botonAceptar.setOnClickListener {
            val clienteEncontrado = validarFormulario()
            if (clienteEncontrado != null) {

                val clienteId = clienteEncontrado.id
                val cuotaPosicion = spinnerCuotaPendiente.selectedItemPosition
                val cuotaObjetoSeleccionado = listaCuotasPendientesDB[cuotaPosicion - 1]
                val idCuotaAPagar = cuotaObjetoSeleccionado.IdCuota
                val montoCuota = cuotaObjetoSeleccionado.Monto
                val formaPagoUI = spinnerPago.selectedItem.toString()
                val cantCuotasTarjeta = spinnerCuotasTarjeta.selectedItem.toString()
                val numTarjeta = etNumeroTarjeta.text.toString().trim()
                val ultDigitos = if (numTarjeta.length >= 4) numTarjeta.takeLast(4) else ""
                val formaPagoDB = if (formaPagoUI == "Tarjeta de crédito") {
                    "Tarjeta"
                } else {
                    "Efectivo"
                }
                val fechaDeHoy = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.LocalDate.now().toString()
                } else {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    sdf.format(Date())
                }

                val clienteNombre = spinnerCliente.selectedItem.toString()
                val cuotaFormateada = cuotaObjetoSeleccionado.cuotaMesAnoUI
                val montoString = montoEditText.text.toString().trim()
                //Para recibo
                val fechaFormateada = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val localDate = java.time.LocalDate.parse(fechaDeHoy) // parsea yyyy-MM-dd
                    localDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                } else {
                    val sdfEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sdfSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = sdfEntrada.parse(fechaDeHoy)
                    sdfSalida.format(date!!)
                }
                //Se inicia la transacción
                val db = dbHelper.writableDatabase
                db.beginTransaction()

                try{
                    dbHelper.actualizarCuotaPagada(
                        db,
                        idCuotaAPagar,
                        fechaDeHoy,
                        formaPagoDB,
                        cantCuotasTarjeta,
                        ultDigitos
                    )
                    dbHelper.crearSiguienteCuota(
                        db,
                        clienteId,
                        montoCuota,
                        cuotaObjetoSeleccionado.FechaVencimiento
                    )
                    db.setTransactionSuccessful()

                    AlertDialog.Builder(this)
                        .setTitle("Pago Registrado")
                        .setMessage("El pago se ha registrado exitosamente. Se generó la próxima cuota.")
                        .setPositiveButton("Aceptar") { dialog, _ ->

                            //  Navegar al Recibo
                            val intent = Intent(this, ReciboSocioActivity::class.java).apply {
                                putExtra("nombreCliente", clienteNombre)
                                putExtra("cuotaPendiente", spinnerCuotaPendiente.selectedItem.toString()) // "Vto:..." para exportar
                                putExtra("cuotaFormateada", cuotaFormateada) // "Cuota Mes..." para mostrar
                                putExtra("monto", montoString)
                                putExtra("formaPago", formaPagoUI)
                                putExtra("cantCuotasTarjeta", cantCuotasTarjeta)
                                putExtra("telefono", clienteEncontrado.Telefono)
                                putExtra("ins", fechaFormateada)
                                putExtra("direccion", clienteEncontrado.Direccion)
                            }
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }catch (e: Exception) {
                    Log.e("RegistroPagoSocio", "Error en la transacción de pago", e)
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No se pudo registrar el pago: ${e.message}")
                        .setPositiveButton("Cerrar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                }finally {
                    db.endTransaction()
                    db.close()
                }
            }
        }

        val botonLimpiar: Button = findViewById(R.id.btnLimpiarSocio)
        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.contentLayout)

            if (vieneConClienteSeleccionado) {
                // no limpiar el spinner del cliente
                limpiarFormulario(rootLayout, spinnerCliente)

                // limpiar siempre
                spinnerCuotaPendiente.setSelection(0)
                spinnerPago.setSelection(0)

            } else {
                // limpiar todo normalmente
                limpiarFormulario(rootLayout)

                spinnerCliente.setSelection(0)
                spinnerCuotaPendiente.setSelection(0)
                spinnerPago.setSelection(0)
            }
        }
    }

    // Cuando se elige un cliente, se actualiza el spinner con las cuotas pendientes
    private fun actualizarSpinnerCuotas(cuotas: List<Cuotas>) {
        adapterCuotas.clear()

        if (cuotas.isEmpty()) {
            adapterCuotas.add("Sin cuotas pendientes")
            spinnerCuotaPendiente.isEnabled = false
        } else {
            // Si hay cuotas, las formateamos y las agregamos
            adapterCuotas.add("Seleccionar cuota a pagar...")
            // Formateamos la cuota
            val nombresCuotas = cuotas.map {
                "Vto: ${it.fechaVencimientoUI} - $${it.Monto.toInt()}"
            }
            adapterCuotas.addAll(nombresCuotas)
            spinnerCuotaPendiente.isEnabled = true
        }

        adapterCuotas.notifyDataSetChanged()
        spinnerCuotaPendiente.setSelection(0)
    }
}