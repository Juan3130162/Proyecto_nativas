package com.example.proyecto_nativas.models

data class ProductoSQLite(
    val productoId: String = "",
    val nombre: String = "",
    val precio: Int = 0,
    val cantidad: Int = 0,
    val imagenUrl: String = ""
)
