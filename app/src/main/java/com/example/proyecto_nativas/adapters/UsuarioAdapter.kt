package com.example.proyecto_nativas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioAdapter(
    private var listaUsuarios: List<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreUsuario)
        val txtEmail: TextView = itemView.findViewById(R.id.txtCorreoUsuario)
        val switchAdmin: Switch = itemView.findViewById(R.id.switchAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]

        holder.txtNombre.text = usuario.nombre
        holder.txtEmail.text = usuario.email
        holder.switchAdmin.isChecked = usuario.admin

        holder.switchAdmin.setOnCheckedChangeListener { _, isChecked ->
            FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(usuario.id)
                .update("admin", isChecked)
        }
    }

    override fun getItemCount(): Int = listaUsuarios.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        listaUsuarios = nuevaLista
        notifyDataSetChanged()
    }
}
