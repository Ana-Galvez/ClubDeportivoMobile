package com.example.clubdeportivomovile.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SportifyClub.db", null, 1) {
    //Creacion de Tablas
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE clientes(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Nombre TEXT NOT NULL, " +
                    "Apellido TEXT NOT NULL, " +
                    "FechaNacimiento TEXT NOT NULL, " +
                    "DNI INTEGER NOT NULL UNIQUE, " +
                    "Genero TEXT NOT NULL CHECK (Genero IN ('F', 'M', 'Prefiero no decir')), " +
                    "Direccion  TEXT NOT NULL, " +
                    "Telefono TEXT NOT NULL, " +
                    "FechaInscripcion TEXT NOT NULL, " +
                    "AptoFisico INTEGER NOT NULL, " +
                    "Socio INTEGER NOT NULL )"
        )

        db.execSQL(
            "CREATE TABLE actividades(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Nombre TEXT NOT NULL, " +
                    "DiaSemana TEXT NOT NULL, " +
                    "Hora TEXT NOT NULL, " +
                    "Precio REAL NOT NULL)"
        )

        // Ingreso los datos establecidos por el sistema
        db.execSQL("""
        INSERT INTO actividades (Nombre, DiaSemana, Hora, Precio) VALUES
            ('Natación', 'Lunes', '11:00:00', 5000.00),
            ('Natación', 'Jueves', '15:00:00', 5000.00),
            ('Pilates', 'Lunes', '18:00:00', 8000.00),
            ('Pilates', 'Miércoles', '12:00:00', 8000.00),
            ('Tenis', 'Miércoles', '18:00:00', 10000.00),
            ('Tenis', 'Viernes', '20:00:00', 10000.00),
            ('Musculación', 'Martes', '19:00:00', 5000.00),
            ('Musculación', 'Jueves', '19:00:00', 5000.00),
            ('Yoga', 'Lunes', '16:00:00', 5000.00),
            ('Yoga', 'Miércoles', '16:00:00', 5000.00),
            ('Aerobic', 'Martes', '18:00:00', 5000.00),
            ('Aerobic', 'Jueves', '12:00:00', 5000.00),
            ('Danza', 'Lunes', '19:00:00', 8000.00),
            ('Danza', 'Miércoles', '19:00:00', 8000.00),
            ('Danza', 'Viernes', '19:00:00', 8000.00),
            ('Danza', 'Lunes', '11:00:00', 8000.00),
            ('Danza', 'Miércoles', '11:00:00', 8000.00),
            ('Danza', 'Viernes', '11:00:00', 8000.00);
        """.trimIndent()
        )

        db.execSQL(
            "CREATE TABLE roles(" +
                    "RolUsu INTEGER PRIMARY KEY, " +
                    "NomRol TEXT)"
        )

        // Ingreso los datos establecidos por el sistema
        db.execSQL("""
        INSERT INTO roles (RolUsu, NomRol) VALUES
            (120, 'Administrador'),
            (121, 'Empleado');
        """.trimIndent()
        )

        db.execSQL(
            "CREATE TABLE usuarios(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Nombre TEXT NOT NULL, " +
                    "Pass TEXT NOT NULL, " +
                    "RolUsu INTEGER NOT NULL, " +
                    "Activo INTEGER NOT NULL," +
                    "FOREIGN KEY (RolUsu) REFERENCES roles(RolUsu))"
        )

        // Ingreso los datos establecidos por el sistema
        db.execSQL("""
        INSERT INTO usuarios (Nombre, Pass, RolUsu, Activo) VALUES
            ('Ana', '123456', 120, 1),
            ('Juan', '123456', 120, 0),
            ('Maria', '654321', 120, 1);
        """.trimIndent()
        )

        db.execSQL(
            "CREATE TABLE socios(" +
                    "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FechaAltaSocio TEXT NOT NULL, " +
                    "FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)"
        )

        db.execSQL(
            "CREATE TABLE nosocios(" +
                    "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FechaAltaNoSocio TEXT NOT NULL, " +
                    "FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)"
        )

        db.execSQL(
            "CREATE TABLE cuotas(" +
                    "IdCuota INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_cliente INTEGER NOT NULL, " +
                    "Monto REAL NOT NULL, " +
                    "ModoPago TEXT CHECK (ModoPago IN ('Efectivo','Tarjeta')), " +
                    "Estado TEXT NOT NULL CHECK (Estado IN ('Pagada','Pendiente')), " +
                    "FechaPago TEXT, " +
                    "FechaVencimiento TEXT NOT NULL, " +
                    "CantCuotas INTEGER DEFAULT 0, " +
                    "UltDigitosTarj INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE pago_actividad(" +
                    "IdPagoActividad INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_cliente INTEGER NOT NULL, " +
                    "id_actividad INTEGER NOT NULL, " +
                    "FechaPago TEXT NOT NULL, " +
                    "ModoPago TEXT NOT NULL CHECK (ModoPago IN ('Efectivo','Tarjeta')) DEFAULT 'Efectivo', " +
                    "Monto REAL NOT NULL, " +
                    "Estado TEXT NOT NULL CHECK (Estado IN ('Pagada','Pendiente')) DEFAULT 'Pendiente', " +
                    "FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (id_actividad) REFERENCES actividades(id) ON DELETE CASCADE" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clientes")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS roles")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS nosocios")
        db.execSQL("DROP TABLE IF EXISTS cuotas")
        db.execSQL("DROP TABLE IF EXISTS pago_actividad")
        onCreate(db)
    }

    // Insertar datos que se obtienen desde los Form
    fun insertarCliente(nombre:String, apellido:String, fechaNac:String, dni: Int, genero: String, direccion: String, telefono: String, fechaInsc: String, aptoFisico: Boolean, socio: Boolean){
        val db = writableDatabase
        val values = ContentValues()
        values.put("Nombre", nombre)
        values.put("Apellido", apellido)
        values.put("FechaNacimiento", fechaNac)
        values.put("DNI", dni)
        values.put("Genero", genero)
        values.put("Direccion", direccion)
        values.put("Telefono", telefono)
        values.put("FechaInscripcion", fechaInsc)
        values.put("AptoFisico", if (aptoFisico) 1 else 0)
        values.put("Socio", if(socio) 1 else 0)
        db.insert("clientes", null, values)
    }

    // Al tener más de 2 datos utilizo Data Class
    data class Cliente(
        val id: Int,
        val nombre: String,
        val apellido: String,
        val fechaNac: String,
        val dni: Int,
        val genero: String,
        val direccion: String,
        val telefono: String,
        val fechaInsc: String,
        val aptoFisico: Int,
        val socio: Int
    )

    fun obtenerClientes(): List<Cliente>{
        val db = readableDatabase
        val lista = mutableListOf<Cliente>()
        val cursor = db.rawQuery("SELECT * FROM clientes", null)
        if(cursor.moveToFirst()){
            do{
                lista.add(
                    Cliente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getInt(9),
                        cursor.getInt(10)
                    )
                )
            } while(cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun eliminarClientePorId(id: Int) {
    val db = writableDatabase
    db.delete("clientes", "id = ?", arrayOf(id.toString()))
    }

    fun insertarSocio(id_cliente: Int, fechaAltaSoc: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id_cliente", id_cliente)
        values.put("FechaAltaSocio", fechaAltaSoc)
        db.insert("socios", null, values)
    }

    fun obtenerSocios(): List<String>{
        val db = readableDatabase
        val lista = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM socios", null)
        if(cursor.moveToFirst()){
            do{
                val id_cliente = cursor.getInt(0)
                val fechaAlta = cursor.getString(1)
                lista.add("Cliente ID: $id_cliente - Alta: $fechaAlta")
            } while(cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun insertarNoSocio(id_cliente: Int, fechaAltaNoSoc: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id_cliente", id_cliente)
        values.put("FechaAltaNoSocio", fechaAltaNoSoc)
        db.insert("nosocios", null, values)
    }

    fun obtenerNoSocios(): List<String>{
        val db = readableDatabase
        val lista = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM nosocios", null)
        if(cursor.moveToFirst()){
            do{
                val id_cliente = cursor.getInt(0)
                val fechaAlta = cursor.getString(1)
                lista.add("Cliente ID: $id_cliente - Alta No Socio: $fechaAlta")
            } while(cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun insertarCuota(id_cliente: Int, monto: Double, modoPago: String, estado: String, fechaPago: String, fechaVencto: String, cantCuotas: String, ultDigitosTarj: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id_cliente", id_cliente)
        values.put("Monto", monto)
        values.put("ModoPago", modoPago)
        values.put("Estado", estado)
        values.put("FechaPago", fechaPago)
        values.put("FechaVencimiento", fechaVencto)
        values.put("CantCuotas", cantCuotas)
        values.put("UltDigitosTarj", ultDigitosTarj)
        db.insert("cuotas", null, values)
    }

    // Al tener más de 2 datos utilizo Data Class
    data class Cuotas(
        val id: Int,
        val monto: Double,
        val modoPago: String,
        val estado: String,
        val fechaPago: String,
        val fechaVencto: String,
        val cantCuotas: Int,
        val ultDigitosTarj: Int
    )

    fun obtenerCuotas(): List<Cuotas>{
        val db = readableDatabase
        val lista = mutableListOf<Cuotas>()
        val cursor = db.rawQuery("SELECT * FROM cuotas", null)
        if(cursor.moveToFirst()){
            do{
                lista.add(
                    Cuotas(
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getInt(7)
                    )
                )
            } while(cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun insertarPagoActividad(id_cliente: Int, id_actividad: Int, fechaPago: String, modoPago: String, monto: Double, estado: String){
        val db = writableDatabase
        val values = ContentValues()
        values.put("id_cliente", id_cliente)
        values.put("id_actividad", id_actividad)
        values.put("FechaPago", fechaPago)
        values.put("ModoPago", modoPago)
        values.put("Monto", monto)
        values.put("Estado", estado)
        db.insert("pago_actividad", null, values)
    }

    // Al tener más de 2 datos utilizo Data Class
    data class PagoActividad(
        val id: Int,
        val id_cliente: Int,
        val id_actividad: Int,
        val fechaPago: String,
        val modoPago: String,
        val monto: Double,
        val estado: String,
    )

    fun obtenerPagoActividad(): List<PagoActividad>{
        val db = readableDatabase
        val lista = mutableListOf<PagoActividad>()
        val cursor = db.rawQuery("SELECT * FROM pago_actividad", null)
        if(cursor.moveToFirst()){
            do{
                lista.add(
                    PagoActividad(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getString(6)
                    )
                )
            } while(cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}