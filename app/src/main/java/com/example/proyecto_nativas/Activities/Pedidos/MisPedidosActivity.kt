package com.example.proyecto_nativas.Activities.Pedidos

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.PedidoAdapter
import com.example.proyecto_nativas.models.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MisPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerPedidos: RecyclerView
    private lateinit var tvSinPedidos: TextView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_pedidos)

        recyclerPedidos = findViewById(R.id.recyclerPedidos)
        tvSinPedidos = findViewById(R.id.tvSinPedidos)

        recyclerPedidos.layoutManager = LinearLayoutManager(this)

        val userId = auth.currentUser?.uid ?: return

        db.collection("pedidos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val pedidos = result.map { it.toObject(Pedido::class.java) }

                if (pedidos.isEmpty()) {
                    tvSinPedidos.visibility = View.VISIBLE
                    recyclerPedidos.visibility = View.GONE
                } else {
                    tvSinPedidos.visibility = View.GONE
                    recyclerPedidos.visibility = View.VISIBLE
                    recyclerPedidos.adapter = PedidoAdapter(pedidos)
                }
            }
            .addOnFailureListener {
                tvSinPedidos.visibility = View.VISIBLE
                tvSinPedidos.text = "Error al cargar pedidos."
                recyclerPedidos.visibility = View.GONE
            }
    }
}
