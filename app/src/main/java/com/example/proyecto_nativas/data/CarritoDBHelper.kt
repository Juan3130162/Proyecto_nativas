// ✅ PASO 1: Crear clase SQLiteOpenHelper para gestionar el carrito
package com.example.proyecto_nativas.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CarritoDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "carrito.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "carrito"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_PRECIO = "precio"
        const val COLUMN_IMAGEN = "imagen_url"
        const val COLUMN_CANTIDAD = "cantidad"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_PRECIO INTEGER,
                $COLUMN_IMAGEN TEXT,
                $COLUMN_CANTIDAD INTEGER
            )
        """
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun agregarProducto(id: String, nombre: String, precio: Int, imagenUrl: String) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id))

        if (cursor.moveToFirst()) {
            val cantidadActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD))
            val nuevoValor = ContentValues()
            nuevoValor.put(COLUMN_CANTIDAD, cantidadActual + 1)
            db.update(TABLE_NAME, nuevoValor, "$COLUMN_ID = ?", arrayOf(id))
        } else {
            val valores = ContentValues().apply {
                put(COLUMN_ID, id)
                put(COLUMN_NOMBRE, nombre)
                put(COLUMN_PRECIO, precio)
                put(COLUMN_IMAGEN, imagenUrl)
                put(COLUMN_CANTIDAD, 1)
            }
            db.insert(TABLE_NAME, null, valores)
        }

        cursor.close()
        db.close()
    }

    fun obtenerCantidadTotal(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_CANTIDAD) FROM $TABLE_NAME", null)
        var total = 0
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0)
        }
        cursor.close()
        return total
    }
}
