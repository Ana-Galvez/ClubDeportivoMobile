package com.example.clubdeportivomovile

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import java.io.File
import java.io.FileOutputStream

class CarnetActivity : BaseActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private val STORAGE_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)
        drawerLayout = findViewById(R.id.drawer_layout_carnet)

        // Configurar header con botón atrás + hamburguesa
        setupHeader(drawerLayout)
        setupDrawerMenu(R.id.drawer_layout_carnet)
        setupBottomBar("nuevo")

        // Datos del carnet
        val tvNombreSocio = findViewById<TextView>(R.id.tvNombreSocio)
        val tvId = findViewById<TextView>(R.id.tvId)
        val tvDni = findViewById<TextView>(R.id.tvDni)
        val tvSocio = findViewById<TextView>(R.id.tvSocio)
        val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        val tvTelefono = findViewById<TextView>(R.id.tvTelefono)
        val tvInsc = findViewById<TextView>(R.id.tvFechaInsc)

        val nombreCliente = intent.getStringExtra("nombreCompleto") ?: "Sin nombre"
        val id = intent.getIntExtra("id", 0)
        val dni = intent.getIntExtra("dni", 0)
        val socio = intent.getBooleanExtra("socio", false)
        val direccion = intent.getStringExtra("direccion") ?: "Sin dirección"
        val telefono = intent.getStringExtra("telefono") ?: "Sin teléfono"
        val inscrip = intent.getStringExtra("fechaInscripcion") ?: "Sin fecha"

        tvNombreSocio.text = nombreCliente
        tvId.text = id.toString()
        tvDni.text = dni.toString()
        tvSocio.text = if (socio) "Sí" else "No"
        tvDireccion.text = direccion
        tvTelefono.text = telefono
        tvInsc.text = inscrip

        // Botón Emitir Carnet
        val btnEmitir = findViewById<Button>(R.id.btnEmitirCarnet)
        btnEmitir.setOnClickListener {
            if (checkPermission()) {
                generarPdf(nombreCliente, id, dni, socio, direccion, telefono, inscrip)
            } else {
                requestPermission()
            }
        }
    }

    // Generar y guardar PDF
    private fun generarPdf(
        nombre: String,
        id: Int,
        dni: Int,
        socio: Boolean,
        direccion: String,
        telefono: String,
        fechaInsc: String
    ) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText("Comprobante de Inscripción", 50f, 40f, paint)

        paint.textSize = 10f
        paint.isFakeBoldText = false
        canvas.drawText("Nombre: $nombre", 20f, 80f, paint)
        canvas.drawText("ID: $id", 20f, 100f, paint)
        canvas.drawText("DNI: $dni", 20f, 120f, paint)
        canvas.drawText("Socio: ${if (socio) "Sí" else "No"}", 20f, 140f, paint)
        canvas.drawText("Dirección: $direccion", 20f, 160f, paint)
        canvas.drawText("Teléfono: $telefono", 20f, 180f, paint)
        canvas.drawText("Fecha de inscripción: $fechaInsc", 20f, 200f, paint)

        pdfDocument.finishPage(page)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 📱 Android 10 en adelante → usa MediaStore (sin permisos)
                val resolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "Comprobante_${nombre.replace(" ", "_")}.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/ClubDeportivo")
                }

                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    resolver.openOutputStream(it).use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                }
            } else {
                // 📱 Android 9 o menor → usa almacenamiento tradicional
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val carpeta = File(downloadsDir, "ClubDeportivo")
                if (!carpeta.exists()) carpeta.mkdirs()

                val filePath = File(carpeta, "Comprobante_${nombre.replace(" ", "_")}.pdf")
                val outputStream = FileOutputStream(filePath)
                pdfDocument.writeTo(outputStream)
                outputStream.close()
            }

            mostrarAlertaExito()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    // Mostrar alerta de éxito
    private fun mostrarAlertaExito() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Descarga completada")
        builder.setMessage("Se descargó el comprobante con éxito. 📄")
        builder.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    // Permisos de almacenamiento (solo necesarios para Android 9 o menos)
    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true // No necesita permisos en Android 10+
        } else {
            val result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            result == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
