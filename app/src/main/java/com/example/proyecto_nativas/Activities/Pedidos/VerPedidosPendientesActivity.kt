package com.example.proyecto_nativas.Activities.Pedidos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.PedidoAdapter
import com.example.proyecto_nativas.models.Pedido
import com.google.firebase.firestore.FirebaseFirestore

class VerPedidosPendientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtSinPedidos: TextView
    private lateinit var layoutSinPedidos: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_pedidos)

        recyclerView = findViewById(R.id.recyclerPedidos)
        txtSinPedidos = findViewById(R.id.tvSinPedidos)
        layoutSinPedidos = findViewById(R.id.layoutSinPedidos)

        recyclerView.layoutManager = LinearLayoutManager(this)

        FirebaseFirestore.getInstance().collection("pedidos")
            .whereEqualTo("atendido", false)
            .get()
            .addOnSuccessListener { result ->
                val pedidos = result.map { it.toObject(Pedido::class.java) }

                if (pedidos.isEmpty()) {
                    layoutSinPedidos.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    layoutSinPedidos.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    val adapter = PedidoAdapter(pedidos, mostrarBotonAtender = true) { pedido ->
                        marcarComoAtendido(pedido)
                    }
                    recyclerView.adapter = adapter
                }
            }
            .addOnFailureListener {
                layoutSinPedidos.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
    }

    private fun marcarComoAtendido(pedido: Pedido) {
        FirebaseFirestore.getInstance().collection("pedidos")
            .document(pedido.id)
            .update("atendido", true)
            .addOnSuccessListener {
                Toast.makeText(this, "Pedido marcado como atendido", Toast.LENGTH_SHORT).show()
                recreate() // recargar lista actualizada
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar el pedido", Toast.LENGTH_SHORT).show()
            }
    }
}
