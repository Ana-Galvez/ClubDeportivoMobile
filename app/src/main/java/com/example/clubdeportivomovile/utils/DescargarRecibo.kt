package com.example.clubdeportivomovile.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class DescargarRecibo(private val context: Context) {

    private val STORAGE_PERMISSION_CODE = 1001

    fun exportarRecibo(
        monto: String,
        nombreCliente: String,
        inscrip: String,
        telefono: String,
        direccion: String,
        horario: String? = null,
        cuota: String? = null,
        actividad: String? = null,
        formaPago: String? = null,
        onSuccess: () -> Unit
    ) {

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText("Recibo", 50f, 40f, paint)

        paint.textSize = 10f
        paint.isFakeBoldText = false
        canvas.drawText("Nombre: $nombreCliente", 20f, 80f, paint)
        canvas.drawText("Monto: $monto", 20f, 100f, paint)
        canvas.drawText("Fecha de inscripción: $inscrip", 20f, 140f, paint)
        canvas.drawText("Teléfono: $telefono", 20f, 160f, paint)
        canvas.drawText("Dirección: $direccion", 20f, 180f, paint)
        if (horario != null) {
            canvas.drawText("Horario: $horario", 20f, 120f, paint)
        }

        if (cuota != null) {
            canvas.drawText("Cuota: $cuota", 20f, 120f, paint)
        }
        if (actividad != null) {
            canvas.drawText("Actividad: $actividad", 20f, 200f, paint)
        }
        if (formaPago != null) {
            canvas.drawText("Forma de pago: $formaPago", 20f, 200f, paint)
        }
        pdfDocument.finishPage(page)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "Recibo_${nombreCliente.replace(" ", "_")}.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/ClubDeportivo")
                }

                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                }

            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val carpeta = File(downloadsDir, "ClubDeportivo")
                if (!carpeta.exists()) carpeta.mkdirs()

                val filePath = File(carpeta, "Recibo_${nombreCliente.replace(" ", "_")}.pdf")
                val outputStream = FileOutputStream(filePath)
                pdfDocument.writeTo(outputStream)
                outputStream.close()
            }

            onSuccess()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }


    fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            val result = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            result == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
}