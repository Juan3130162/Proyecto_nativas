package com.example.proyecto_nativas.Activities.Pedidos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.PedidoAdapter
import com.example.proyecto_nativas.models.Pedido
import com.google.firebase.firestore.FirebaseFirestore

class VerPedidosAtendidosActivity : AppCompatActivity() {

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
            .whereEqualTo("atendido", true)
            .get()
            .addOnSuccessListener { result ->
                val pedidos = result.map { it.toObject(Pedido::class.java) }

                if (pedidos.isEmpty()) {
                    layoutSinPedidos.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    layoutSinPedidos.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    val adapter = PedidoAdapter(pedidos, mostrarBotonAtender = false)
                    recyclerView.adapter = adapter
                }
            }
            .addOnFailureListener {
                layoutSinPedidos.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
    }
}
