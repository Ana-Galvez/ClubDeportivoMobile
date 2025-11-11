package com.example.clubdeportivomovile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    clientes: List<Cliente>,
    private val onEditar: (Cliente) -> Unit,
    private val onMostrarCarnet: (Cliente) -> Unit,
    private val onEliminar: (Cliente) -> Unit,
    private val onRegistrarPago: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    private val clientes: MutableList<Cliente> = clientes.toMutableList()
    fun updateList(nuevaLista: List<Cliente>) {
        val diffResult = DiffUtil.calculateDiff(ClienteDiffCallback(clientes, nuevaLista))
        clientes.clear()
        clientes.addAll(nuevaLista)
        diffResult.dispatchUpdatesTo(this)
    }
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
        holder.tvNombre.text = cliente.nombreCompleto
        holder.tvTipo.text = if (cliente.esSocio) "Socio" else "No Socio"

        holder.ivEditar.setOnClickListener { onEditar(cliente) }
        holder.ivEliminar.setOnClickListener { onEliminar(cliente) }
        holder.ivVer.setOnClickListener { onMostrarCarnet(cliente) }
        holder.ivPago.setOnClickListener { onRegistrarPago(cliente) }
    }

    override fun getItemCount() = clientes.size
}

class ClienteDiffCallback(
    private val oldList: List<Cliente>,
    private val newList: List<Cliente>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
