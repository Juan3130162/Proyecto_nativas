package com.example.proyecto_nativas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.models.Pedido
import com.example.proyecto_nativas.models.ProductoSQLite

class PedidoAdapter(
    private val pedidos: List<Pedido>,
    private val mostrarBotonAtender: Boolean = false,
    private val onAtenderClick: (Pedido) -> Unit = {}
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIdPedido: TextView = itemView.findViewById(R.id.txtPedidoId)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreUsuario)
        val txtCorreo: TextView = itemView.findViewById(R.id.txtEmailUsuario)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotalPedido)
        val txtProductos: TextView = itemView.findViewById(R.id.txtProductosPedido)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstadoPedido)
        val btnAtender: Button = itemView.findViewById(R.id.btnAtenderPedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        holder.txtIdPedido.text = "ID: ${pedido.id}"
        holder.txtNombre.text = "Nombre: ${pedido.nombreUsuario}"
        holder.txtCorreo.text = "Correo: ${pedido.email}"
        holder.txtTotal.text = "Total: $${pedido.total}"

        val productosResumen = pedido.productos.joinToString(separator = "\n") {
            "• ${it.nombre} x${it.cantidad} - $${it.precio * it.cantidad}"
        }
        holder.txtProductos.text = productosResumen

        if (pedido.atendido) {
            holder.txtEstado.text = "Atendido"
            holder.btnAtender.visibility = View.GONE
        } else {
            holder.txtEstado.text = "Pendiente"
            if (mostrarBotonAtender) {
                holder.btnAtender.visibility = View.VISIBLE
                holder.btnAtender.setOnClickListener {
                    onAtenderClick(pedido)
                }
            } else {
                holder.btnAtender.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = pedidos.size
}
