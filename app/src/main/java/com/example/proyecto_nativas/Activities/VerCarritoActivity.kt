package com.example.proyecto_nativas.Activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.CarritoAdapter
import com.example.proyecto_nativas.data.CarritoRepository
import com.example.proyecto_nativas.models.ProductoSQLite
import com.example.proyecto_nativas.models.Pedido
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class VerCarritoActivity : AppCompatActivity() {

    private lateinit var recyclerCarrito: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnRealizarPedido: MaterialButton
    private lateinit var carritoAdapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_carrito)
        CarritoRepository.init(this)

        recyclerCarrito = findViewById(R.id.recyclerCarrito)
        txtTotal = findViewById(R.id.tvTotal)
        btnRealizarPedido = findViewById(R.id.btnRealizarPedido)

        recyclerCarrito.layoutManager = LinearLayoutManager(this)

        actualizarLista()

        btnRealizarPedido.setOnClickListener {
            val productosEnCarrito = CarritoRepository.obtenerProductos(this)

            if (productosEnCarrito.isNotEmpty()) {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid ?: ""
                val email = user?.email ?: ""
                val nombre = user?.displayName ?: "Usuario"
                val pedidoId = UUID.randomUUID().toString()
                val total = productosEnCarrito.sumOf { it.precio * it.cantidad }

                val pedido = Pedido(
                    id = pedidoId,
                    userId = userId,
                    nombreUsuario = nombre,
                    email = email,
                    productos = productosEnCarrito,
                    total = total
                )

                FirebaseFirestore.getInstance()
                    .collection("pedidos")
                    .document(pedidoId)
                    .set(pedido)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pedido realizado correctamente", Toast.LENGTH_SHORT).show()
                        CarritoRepository.vaciarCarrito(this)
                        actualizarLista()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this, "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarLista() {
        val nuevosProductos = CarritoRepository.obtenerProductos(this)

        if (!::carritoAdapter.isInitialized) {
            carritoAdapter = CarritoAdapter(
                listaCarrito  = nuevosProductos,
                onIncrement = { producto ->
                    CarritoRepository.agregarProducto(this, producto)
                    actualizarLista()
                },
                onDecrement = { producto ->
                    CarritoRepository.reducirProducto(this, producto)
                    actualizarLista()
                }
            )
            recyclerCarrito.adapter = carritoAdapter
        } else {
            carritoAdapter.actualizarDatos(nuevosProductos)
        }

        actualizarTotal(nuevosProductos)
    }

    private fun actualizarTotal(productos: List<ProductoSQLite>) {
        val total = productos.sumOf { it.precio * it.cantidad }
        txtTotal.text = "Total: $$total"
    }
}

