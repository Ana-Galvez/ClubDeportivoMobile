package com.example.clubdeportivomovile

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MorososAdapter(
    morosos: List<Moroso>
) : RecyclerView.Adapter<MorososAdapter.MViewHolder>() {

    private val listaOriginal: MutableList<Moroso> = morosos.toMutableList()
    private val listaFiltrada: MutableList<Moroso> = morosos.toMutableList()

    inner class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        val tvMonto: TextView = itemView.findViewById(R.id.tvMonto)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moroso, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        val item = listaFiltrada[position]
        holder.tvNombre.text = item.nombreCompleto
        holder.tvId.text = "ID: ${item.id}"
        holder.tvDni.text = "DNI: ${item.dni}"
        holder.tvMonto.text = "Monto: ${item.totalAdeudadoUI}"
        holder.tvTelefono.text = "Teléfono: ${item.telefono}"
    }

    override fun getItemCount(): Int = listaFiltrada.size

    fun updateList(nuevaLista: List<Moroso>) {
        listaOriginal.clear()
        listaOriginal.addAll(nuevaLista)
        listaFiltrada.clear()
        listaFiltrada.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun filtrar(query: String) {
        val q = query.trim().lowercase()
        listaFiltrada.clear()
        if (q.isEmpty()) {
            listaFiltrada.addAll(listaOriginal)
        } else {
            listaFiltrada.addAll(
                listaOriginal.filter {
                    it.nombreCompleto.lowercase().contains(q) ||
                            it.dni.toString().contains(q)
                }
            )
        }
        notifyDataSetChanged()
    }
}
