package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class EliminarCliente : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_eliminar_cliente, container, false)
    }

    companion object {
        fun newInstance(id: Int, nombre: String): EliminarCliente {
            val modal = EliminarCliente()
            val args = Bundle()
            args.putInt("id_cliente", id)
            args.putString("nombreCompleto", nombre)
            modal.arguments = args
            return modal
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //PAso datos del cliente a eliminar
        val nombre = arguments?.getString("nombreCompleto") ?: ""
        val id = arguments?.getInt("id_cliente") ?: -1

        val txtMensaje = view.findViewById<TextView>(R.id.txt_mensaje_eliminar)
        txtMensaje.text = "¿Seguro que quieres eliminar al cliente $nombre?"

        val btnSi = view.findViewById<Button>(R.id.btn_si_eliminar)

        btnSi.setOnClickListener {
            Toast.makeText(requireContext(), "Se eliminó con exito al cliente $nombre", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        val btnNo = view.findViewById<Button>(R.id.btn_no_eliminar)
        btnNo.setOnClickListener { dismiss() }//Solo cierra el modal
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}