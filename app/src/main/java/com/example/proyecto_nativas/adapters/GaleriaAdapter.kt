package com.example.proyecto_nativas.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.Activities.Galeria.FullScreenImageActivity

class GaleriaAdapter(
    private val listaImagenes: List<String>,
    private val context: Context
) : RecyclerView.Adapter<GaleriaAdapter.ImagenViewHolder>() {

    inner class ImagenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgGaleria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_galeria, parent, false)
        return ImagenViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        val url = listaImagenes[position]

        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgProducto)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FullScreenImageActivity::class.java)
            intent.putExtra("imagen_url", url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaImagenes.size
}
