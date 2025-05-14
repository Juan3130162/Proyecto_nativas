package com.example.proyecto_nativas.models

data class CarritoItem(
    val productoId: String,
    val nombre: String,
    val precio: Int,
    var cantidad: Int
)
