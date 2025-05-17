package com.example.proyecto_nativas.models

data class Pedido(
    val id: String = "",
    val userId: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val productos: List<ProductoSQLite> = emptyList(),
    val total: Int = 0,
    val atendido: Boolean = false, // indica si fue gestionado
    val timestamp: Long = System.currentTimeMillis()
)
