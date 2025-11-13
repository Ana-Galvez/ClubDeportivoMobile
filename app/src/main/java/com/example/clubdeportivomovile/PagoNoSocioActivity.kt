package com.example.clubdeportivomovile

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clubdeportivomovile.data.DBHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log

class PagoNoSocioActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: DBHelper
    private var listaDeActividadesDB: List<Actividad> = listOf() // Va a guardar las actividades ingresadas en la DB
    private var listaNoSociosDB: List<Cliente> = listOf() // Guarda los NO SOCIOS de la DB
    private lateinit var spinnerActividad: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pago_no_socios)
        setupDrawerMenu(R.id.drawer_layout_no_socio) ///********** agregue para fc del menu ---va el id como parametro
        setupBottomBar("pagos")

        dbHelper = DBHelper(this)

        // Cliente
        listaNoSociosDB = dbHelper.obtenerNoSociosClientes()

        val spinnerCliente: Spinner= findViewById(R.id.spinner_cliente_no_socio)

        val nombresClientesSpinner = mutableListOf("Seleccionar cliente...")
        nombresClientesSpinner.addAll(listaNoSociosDB.map { it.nombreCompleto })

        val adapterClientes = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom, // Layout para la caja cerrada
            nombresClientesSpinner
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        adapterClientes.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerCliente.adapter = adapterClientes

        //Para no mostrar sugerencia de busqueda de nombres, ya que viene del listado de cliente
        val clienteSeleccionado = intent.getStringExtra("clienteSeleccionado")

        if (clienteSeleccionado != null) {
            val posicion = nombresClientesSpinner.indexOf(clienteSeleccionado)
            if(posicion >=0){
                spinnerCliente.setSelection(posicion)
                spinnerCliente.isEnabled = false
            }else {
                spinnerCliente.setSelection(0) // Mostrar "Seleccionar cliente..."
            }
        }

        //actividades
        listaDeActividadesDB = dbHelper.obtenerActividades()

        spinnerActividad= findViewById(R.id.spinner_pago_no_socio)
        val horarioEditText: EditText = findViewById(R.id.formNoSocioHorario)
        val montoEditText: EditText = findViewById(R.id.formNoSocioMonto)

        val nombresActividades = mutableListOf("Seleccionar actividad...")
        nombresActividades.addAll(listaDeActividadesDB.map { it.nombre })

        val adapterActividades = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_item_custom,
            nombresActividades
        ) {
            override fun isEnabled(position: Int): Boolean {
                // placeholder
                return position != 0
            }
        }

        adapterActividades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActividad.adapter = adapterActividades

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

                val actividadSeleccionada = listaDeActividadesDB[position - 1] // -1 por el placeholder
                horarioEditText.setText(actividadSeleccionada.hora)
                montoEditText.setText(actividadSeleccionada.monto.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerActividad.setSelection(0)

        adapterActividades.setDropDownViewResource(R.layout.spinner_item_custom)
        spinnerActividad.adapter = adapterActividades

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

                val actividadSeleccionada = listaDeActividadesDB[position - 1] // -1 por el placeholder
                horarioEditText.setText("${actividadSeleccionada.diaSemana} ${actividadSeleccionada.hora}")
                montoEditText.setText(actividadSeleccionada.monto.toString())
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
            spinnerCliente: Spinner
        ): Cliente? {

            val clientePosicion = spinnerCliente.selectedItemPosition
            val actividadPosicion = spinnerActividad.selectedItemPosition
            val clienteSeleccionado = clientePosicion != 0
            val actividadSeleccionada = actividadPosicion != 0

            // campos vacíos
            if (!clienteSeleccionado && !actividadSeleccionada) {
                Toast.makeText(
                    this,
                    "Debe seleccionar un cliente y una actividad.",
                    Toast.LENGTH_SHORT
                ).show()
                return null
            } else if (!clienteSeleccionado) {
                Toast.makeText(this, "Debe seleccionar un cliente.", Toast.LENGTH_SHORT)
                    .show()
                return null
            } else if (!actividadSeleccionada) {
                Toast.makeText(this, "Debe seleccionar una actividad.", Toast.LENGTH_SHORT).show()
                return null
            }

            val clienteEncontrado = listaNoSociosDB[clientePosicion - 1]
            // Verificar si es no socio (en principio, solo debe mostrar los clientes NO socios)
            if (clienteEncontrado.esSocio) {
                Toast.makeText(
                    this,
                    "El cliente es socio no se puede registrar el pago.",
                    Toast.LENGTH_LONG
                ).show()
                return null
            }
            return clienteEncontrado
        }

        //Botones
        val botonAceptar: Button = findViewById(R.id.BotonAceptarPagoNoSocio)

        botonAceptar.setOnClickListener {
            val clienteEncontrado = validarFormulario(spinnerActividad, spinnerCliente)
            if (clienteEncontrado != null) {
                val clienteId = clienteEncontrado.id
                val actividadPosicion = spinnerActividad.selectedItemPosition
                val actividadObjeto = listaDeActividadesDB[actividadPosicion - 1]
                val actividadId = actividadObjeto.id
                val montoDouble = actividadObjeto.monto
                val fechaDeHoy = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.LocalDate.now().toString()

                } else {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    sdf.format(Date())
                }
                val estado = "Pagada"

                //Intent
                val clienteNombre = spinnerCliente.selectedItem.toString()
                val actividadSeleccionada = spinnerActividad.selectedItem.toString()
                val horario = horarioEditText.text.toString().trim()
                val montoString = montoEditText.text.toString().trim()

                try{
                    dbHelper.insertarPagoActividad(
                        clienteId,
                        actividadId,
                        fechaDeHoy,
                        montoDouble,
                        estado
                    )

                    AlertDialog.Builder(this)
                        .setTitle("Pago Registrado")
                        .setMessage("El pago se ha registrado exitosamente.")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            val intent = Intent(this, ReciboPagoNoSocioActivity::class.java).apply {
                                putExtra("nombreCliente", clienteNombre)
                                putExtra("actividad", actividadSeleccionada)
                                putExtra("horario", horario)
                                putExtra("monto", montoString)
                                putExtra("telefono", clienteEncontrado.Telefono)
                                putExtra("ins", clienteEncontrado.fechaInscripcionUI)
                                putExtra("direccion", clienteEncontrado.Direccion)
                            }
                            startActivity(intent)
                            dialog.dismiss()
                    }
                    .setCancelable(false) // Evita que se cierre al tocar fuera
                    .show()
                } catch (e: Exception) {
                    // 6. En caso de error en la DB, mostrar un Toast de error
                    Log.e("PagoNoSocioActivity", "Error al insertar en la base de datos", e)
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No se pudo registrar el pago. Por favor, intente de nuevo.")
                        .setPositiveButton("Cerrar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        val botonLimpiar: Button = findViewById(R.id.BotonLimpiarPagoNoSocio)

        botonLimpiar.setOnClickListener {
            val rootLayout = findViewById<ViewGroup>(R.id.content_Layout)
            limpiarFormulario(rootLayout)
            spinnerCliente.setSelection(0)
            spinnerActividad.setSelection(0)
        }

        //activo botones barra
        setupBottomBar("pagos")
    }
}

