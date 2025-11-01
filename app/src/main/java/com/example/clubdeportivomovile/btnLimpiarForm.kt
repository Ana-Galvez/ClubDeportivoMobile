package com.example.clubdeportivomovile

import android.view.ViewGroup
import android.widget.*

fun limpiarFormulario(viewGroup: ViewGroup) {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        when (child) {
            is EditText -> child.text.clear()
            is TextView -> {
                if (child.id == R.id.tvFechaNac || child.id == R.id.tvNoSocioFechaInsc) {
                    child.text = ""
                }
            }
            is CheckBox -> child.isChecked = false
            is Switch -> child.isChecked = false
            is Spinner -> child.setSelection(0)
            is AutoCompleteTextView -> child.setText("", false)
            is RadioGroup -> child.clearCheck()
            is ViewGroup -> limpiarFormulario(child)
        }
    }
}
