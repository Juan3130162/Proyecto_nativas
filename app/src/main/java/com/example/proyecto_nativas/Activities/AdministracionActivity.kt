package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.Activities.Pedidos.VerPedidosAtendidosActivity
import com.example.proyecto_nativas.Activities.Pedidos.VerPedidosPendientesActivity

class AdministracionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administracion)

        val btnAgregarProducto = findViewById<Button>(R.id.btnAgregarProductoAdmin)
        val btnVerUsuarios = findViewById<Button>(R.id.btnVerUsuarios)
        val btnPedidosSinAtender = findViewById<Button>(R.id.btnPedidosSinAtender)
        val btnPedidosAtendidos = findViewById<Button>(R.id.btnPedidosAtendidos)

        btnAgregarProducto.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        btnVerUsuarios.setOnClickListener {
            startActivity(Intent(this, VerUsuariosActivity::class.java))
        }

        btnPedidosSinAtender.setOnClickListener {
            startActivity(Intent(this, VerPedidosPendientesActivity::class.java))
        }

        btnPedidosAtendidos.setOnClickListener {
            startActivity(Intent(this, VerPedidosAtendidosActivity::class.java))
        }
    }
}
