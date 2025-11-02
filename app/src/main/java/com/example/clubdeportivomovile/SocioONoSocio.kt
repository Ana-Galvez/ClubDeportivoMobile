package com.example.clubdeportivomovile

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.widget.Button
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SocioONoSocio : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_socio_ono_socio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnSi = view.findViewById<Button>(R.id.btn_si)
        btnSi.setOnClickListener {
            startActivity(Intent(requireActivity(), RegistroPagoSocio::class.java))
            dismiss()
        }

        val btnNo = view.findViewById<Button>(R.id.btn_no)
        btnNo.setOnClickListener {
            startActivity(Intent(requireActivity(), PagoNoSocioActivity::class.java))
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

