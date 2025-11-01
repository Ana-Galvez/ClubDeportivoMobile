package com.example.clubdeportivomovile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onEditar: (Cliente) -> Unit,
    private val onMostrarCarnet: (Cliente) -> Unit,
    private val onEliminar: (Cliente) -> Unit,
    private val onRegistrarPago: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvClienteNombre)
        val tvTipo: TextView = itemView.findViewById(R.id.tvClienteTipo)
        val ivEditar: ImageView = itemView.findViewById(R.id.ivEditarCliente)
        val ivEliminar: ImageView = itemView.findViewById(R.id.ivEliminarCliente)
        val ivVer: ImageView = itemView.findViewById(R.id.ivVerCliente)
        val ivPago: ImageView = itemView.findViewById(R.id.ivRegistrarPago)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.tvNombre.text = cliente.nombre+ " " + cliente.apellido
        holder.tvTipo.text = if (cliente.socio) "Socio" else "No Socio"

        holder.ivEditar.setOnClickListener { onEditar(cliente) }
        holder.ivEliminar.setOnClickListener { onEliminar(cliente) }
        holder.ivVer.setOnClickListener { onMostrarCarnet(cliente) }
        holder.ivPago.setOnClickListener { onRegistrarPago(cliente) }
    }

    override fun getItemCount() = clientes.size
}

