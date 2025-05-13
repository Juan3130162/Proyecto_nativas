package com.example.proyecto_nativas.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.data.CarritoRepository
import com.example.proyecto_nativas.models.Producto
import com.example.proyecto_nativas.models.ProductoSQLite

class ProductoAdapter(
    private val listaProductos: List<Producto>,
    private val onAddToCartClick: (Producto) -> Unit,
    private val onItemClick: (Producto) -> Unit
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
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgProducto)

        holder.btnAgregar.setOnClickListener {
            val productoSQLite = ProductoSQLite(
                productoId = producto.id,
                nombre = producto.nombre,
                precio = producto.precio,
                cantidad = 1,
                imagenUrl = producto.imagen_url
            )

            CarritoRepository.agregarProducto(holder.itemView.context, productoSQLite)

            val cantidadTotal = CarritoRepository.contarProductos(holder.itemView.context)
            val intent = Intent("ACTUALIZAR_CARRITO")
            intent.putExtra("cantidad_total", cantidadTotal)
            holder.itemView.context.sendBroadcast(intent)

            Toast.makeText(holder.itemView.context, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
            onAddToCartClick(producto)
        }

        holder.itemView.setOnClickListener {
            onItemClick(producto)
        }
    }

    override fun getItemCount(): Int = listaProductos.size
}
