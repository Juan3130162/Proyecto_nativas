package com.example.proyecto_nativas.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proyecto_nativas.Activities.*
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth

class BottomNavFragment : Fragment() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnCart: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var tvUserEmail: TextView
    private lateinit var cartBadge: TextView

    private var userEmail: String? = null

    var cartItemCount: Int = 0
        set(value) {
            field = value
            updateCartBadge()
        }

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

        // Obtener email desde el intent de la activity que contiene el fragment
        userEmail = activity?.intent?.getStringExtra("email")
        tvUserEmail.text = userEmail ?: "Usuario"

        btnHome.setOnClickListener {
            val intent = Intent(requireContext(), ProductActivity::class.java)
            intent.putExtra("email", userEmail)
            startActivity(intent)
        }

        btnCart.setOnClickListener {
            val intent = Intent(requireContext(), ViewCarActivity::class.java)
            intent.putExtra("email", userEmail)
            startActivity(intent)
        }

        btnProfile.setOnClickListener { showProfileMenu(it) }

        updateCartBadge()

        return view
    }

    private fun updateCartBadge() {
        if (cartItemCount > 0) {
            cartBadge.text = cartItemCount.toString()
            cartBadge.visibility = View.VISIBLE
        } else {
            cartBadge.visibility = View.GONE
        }
    }

    private fun showProfileMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.menu_profile, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_profile -> {
                    val intent = Intent(requireContext(), ProfileActivity::class.java)
                    intent.putExtra("email", userEmail)
                    startActivity(intent)
                    true
                }
                R.id.agregar_producto -> {
                    val intent = Intent(requireContext(), AddProductActivity::class.java)
                    intent.putExtra("email", userEmail)
                    startActivity(intent)
                    true
                }

                R.id.lista_de_producto -> {
                    val intent = Intent(requireContext(), ListaProductosActivity::class.java)
                    intent.putExtra("email", userEmail)
                    startActivity(intent)
                    true
                }
                R.id.menu_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    true
                }
                else -> false
            }
        }

        popup.show()
    }
}
