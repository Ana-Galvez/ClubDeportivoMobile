package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
class Apto_fisico : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_apto_fisico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnSi = view.findViewById<Button>(R.id.btn_si)
        btnSi.setOnClickListener {
            startActivity(Intent(requireActivity(), Registrar::class.java))
            dismiss()
        }

        val btnNo = view.findViewById<Button>(R.id.btn_no)
        btnNo.setOnClickListener {
            //startActivity(Intent(requireActivity(), PagoNoSocioActivity::class.java))
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