// Ruta sugerida: com.example.proyecto_nativas.data.CarritoRepository.kt

package com.example.proyecto_nativas.data

import android.content.Context
import com.example.proyecto_nativas.models.ProductoSQLite

object CarritoRepository {

    private lateinit var dbHelper: CarritoDBHelper

    fun init(context: Context) {
        dbHelper = CarritoDBHelper(context.applicationContext)
    }

    fun agregarProducto(context: Context, producto: ProductoSQLite) {
        val db = dbHelper.writableDatabase

        val cursor = db.rawQuery(
            "SELECT cantidad FROM carrito WHERE id = ?",
            arrayOf(producto.productoId)
        )
        if (cursor.moveToFirst()) {
            val cantidadActual = cursor.getInt(0)
            val nuevaCantidad = cantidadActual + producto.cantidad
            db.execSQL(
                "UPDATE carrito SET cantidad = ? WHERE id = ?",
                arrayOf(nuevaCantidad, producto.productoId)
            )
        } else {
            db.execSQL(
                "INSERT INTO carrito (id, nombre, precio, cantidad, imagen_url) VALUES (?, ?, ?, ?, ?)",
                arrayOf(producto.productoId, producto.nombre, producto.precio, producto.cantidad, producto.imagenUrl)
            )
        }
        cursor.close()
    }

    fun reducirProducto(context: Context, producto: ProductoSQLite) {
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery(
            "SELECT cantidad FROM carrito WHERE id = ?",
            arrayOf(producto.productoId)
        )
        if (cursor.moveToFirst()) {
            val cantidadActual = cursor.getInt(0)
            val nuevaCantidad = cantidadActual - 1
            if (nuevaCantidad <= 0) {
                db.execSQL("DELETE FROM carrito WHERE id = ?", arrayOf(producto.productoId))
            } else {
                db.execSQL(
                    "UPDATE carrito SET cantidad = ? WHERE id = ?",
                    arrayOf(nuevaCantidad, producto.productoId)
                )
            }
        }
        cursor.close()
    }

    fun obtenerProductos(context: Context): List<ProductoSQLite> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, precio, cantidad, imagen_url FROM carrito", null)
        val lista = mutableListOf<ProductoSQLite>()

        if (cursor.moveToFirst()) {
            do {
                val item = ProductoSQLite(
                    productoId = cursor.getString(0),
                    nombre = cursor.getString(1),
                    precio = cursor.getInt(2),
                    cantidad = cursor.getInt(3),
                    imagenUrl = cursor.getString(4) ?: ""
                )
                lista.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun vaciarCarrito(context: Context) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM carrito")
    }

    fun contarProductos(context: Context): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(cantidad) FROM carrito", null)
        val total = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return total
    }
}
