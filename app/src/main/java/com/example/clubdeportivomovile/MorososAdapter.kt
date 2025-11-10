package com.example.clubdeportivomovile

import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

class MorososAdapter(private val listaOriginal: List<MorososClaseRV>) :
    RecyclerView.Adapter<MorososAdapter.MorososViewHolder>(){
    private var listaFiltrada = listaOriginal.toMutableList()

    override fun getItemCount() = listaFiltrada.size

    fun filtrar (query: String){
        val texto = query.lowercase()
        listaFiltrada = if (texto.isEmpty()){
            listaOriginal.toMutableList()
        }else{
            listaOriginal.filter{
                it.dni.contains(texto) || it.nombre.lowercase().contains(texto)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
    inner class MorososViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val tvId = itemView.findViewById<TextView>(R.id.tvId)
        val tvDni = itemView.findViewById<TextView>(R.id.tvDni)
        val tvMonto = itemView.findViewById<TextView>(R.id.tvMonto)
        val tvTelefono = itemView.findViewById<TextView>(R.id.tvTelefono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MorososViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_moroso, parent, false)
        return MorososViewHolder(view)
    }

    override fun onBindViewHolder(holder: MorososViewHolder, position: Int) {
        val item = listaFiltrada[position]
        holder.tvNombre.text = item.nombre
        holder.tvId.text = "ID: ${item.id}"
        holder.tvDni.text = "DNI: ${item.dni}"
        holder.tvMonto.text = "Monto: ${item.monto}"
        holder.tvTelefono.text = "Teléfono: ${item.telefono}"
    }

}