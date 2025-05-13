package com.example.proyecto_nativas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.models.ProductoSQLite

class CarritoAdapter(
    private var listaCarrito: List<ProductoSQLite>,
    private val onIncrement: (ProductoSQLite) -> Unit,
    private val onDecrement: (ProductoSQLite) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProductoCarrito)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreCarrito)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioCarrito)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadCarrito)
        val btnSumar: Button = itemView.findViewById(R.id.btnSumar)
        val btnRestar: Button = itemView.findViewById(R.id.btnRestar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = listaCarrito[position]

        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = "$${producto.precio}"
        holder.txtCantidad.text = "Cantidad: ${producto.cantidad}"

        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgProducto)

        holder.btnSumar.setOnClickListener {
            onIncrement(producto)
        }

        holder.btnRestar.setOnClickListener {
            onDecrement(producto)
        }
    }

    override fun getItemCount(): Int = listaCarrito.size

    fun actualizarDatos(nuevaLista: List<ProductoSQLite>) {
        listaCarrito = nuevaLista
        notifyDataSetChanged()
    }
}
