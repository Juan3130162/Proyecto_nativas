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
import com.example.proyecto_nativas.models.Producto

class ProductoAdapter(
    private val listaProductos: List<Producto>,
    private val onAddToCartClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreProducto)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioProducto)
        val btnAgregar: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = "$${producto.precio}"

        Glide.with(holder.itemView.context)
            .load(producto.imagen_url)
            .placeholder(R.drawable.ic_placeholder) // opcional
            .into(holder.imgProducto)

        holder.btnAgregar.setOnClickListener {
            onAddToCartClick(producto)
        }
    }

    override fun getItemCount(): Int = listaProductos.size
}
