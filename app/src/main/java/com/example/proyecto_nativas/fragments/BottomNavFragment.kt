package com.example.proyecto_nativas.fragments

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.proyecto_nativas.Activities.*
import com.example.proyecto_nativas.Activities.LoginAlternativo.InicioSesionActivity
import com.example.proyecto_nativas.Activities.Pedidos.MisPedidosActivity
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.data.CarritoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BottomNavFragment : Fragment() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnCart: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var tvUserEmail: TextView
    private lateinit var cartBadge: TextView

    private var userEmail: String? = null
    private lateinit var carritoReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        btnHome = view.findViewById(R.id.btnHome)
        btnCart = view.findViewById(R.id.btnCart)
        btnProfile = view.findViewById(R.id.btnProfile)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)

        cartBadge = view.findViewById(R.id.cartBadge)

        userEmail = FirebaseAuth.getInstance().currentUser?.email
        tvUserEmail.text = userEmail ?: "Usuario"

        btnHome.setOnClickListener {
            val intent = Intent(requireContext(), ListaProductosActivity::class.java)
            startActivity(intent)
        }

        btnCart.setOnClickListener {
            val intent = Intent(requireContext(), VerCarritoActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener { showProfileMenu(it) }

        CarritoRepository.init(requireContext()) // Inicializa el repositorio
        updateCartBadge()

        // 📡 Escuchar el broadcast desde ProductoAdapter
        carritoReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val cantidad = intent?.getIntExtra("cantidad_total", 0) ?: 0
                updateCartBadge(cantidad)
            }
        }

        ContextCompat.registerReceiver(
            requireContext().applicationContext,
            carritoReceiver,
            IntentFilter("ACTUALIZAR_CARRITO"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        return view
    }

    private fun updateCartBadge(cantidad: Int = CarritoRepository.contarProductos(requireContext())) {
        if (cantidad > 0) {
            cartBadge.text = cantidad.toString()
            cartBadge.visibility = View.VISIBLE
        } else {
            cartBadge.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            requireContext().applicationContext.unregisterReceiver(carritoReceiver)
        } catch (e : IllegalArgumentException) {
            // Ya fue desregistrado o nunca registrado
        }
    }

    private fun showProfileMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.menu_profile, popup.menu)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        // Consultar si es admin
        FirebaseFirestore.getInstance().collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                val esAdmin = document.getBoolean("admin") ?: false

                if (!esAdmin) {
                    popup.menu.findItem(R.id.menu_admin)?.isVisible = false
                }

                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_profile -> {
                            startActivity(Intent(requireContext(), ProfileActivity::class.java))
                            true
                        }
                        R.id.menu_mis_pedidos -> {
                            startActivity(Intent(requireContext(), MisPedidosActivity::class.java))
                            true
                        }
                        R.id.menu_admin -> {
                            startActivity(Intent(requireContext(), AdministracionActivity::class.java))
                            true
                        }
                        R.id.lista_de_producto -> {
                            startActivity(Intent(requireContext(), ListaProductosActivity::class.java))
                            true
                        }
                        R.id.menu_logout -> {
                            FirebaseAuth.getInstance().signOut()
                            startActivity(Intent(requireContext(), InicioSesionActivity::class.java))
                            requireActivity().finish()
                            true
                        }
                        else -> false
                    }
                }


                popup.show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al verificar permisos", Toast.LENGTH_SHORT).show()
            }
    }
}
