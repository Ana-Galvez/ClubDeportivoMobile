import android.view.ViewGroup
import android.widget.EditText

fun limpiarFormulario(viewGroup: ViewGroup) {
    for (i in 0 until viewGroup.childCount) {
        val view = viewGroup.getChildAt(i)
        when (view) {
            is EditText -> view.text.clear() // Limpia el texto
            is ViewGroup -> limpiarFormulario(view) // Recorre recursivamente otros layouts
        }
    }
}
