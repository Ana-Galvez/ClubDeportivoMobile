package com.example.clubdeportivomovile.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.clubdeportivomovile.Actividad
import com.example.clubdeportivomovile.Cliente
import com.example.clubdeportivomovile.Cuotas
import com.example.clubdeportivomovile.PagoActividad

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SportifyClub.db", null, 1) {

    //Para eliminación en cascada
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
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
                    "Monto REAL NOT NULL)"
        )

        // Ingreso los datos establecidos por el sistema
        db.execSQL(
            """
        INSERT INTO actividades (Nombre, DiaSemana, Hora, Monto) VALUES
            ('Natación', 'Lunes', '11:00:00', 5000.00),
            ('Pilates', 'Lunes', '18:00:00', 8000.00),
            ('Tenis', 'Miércoles', '18:00:00', 10000.00),
            ('Musculación', 'Martes', '19:00:00', 5000.00),
            ('Yoga', 'Lunes', '16:00:00', 5000.00),
            ('Aerobic', 'Martes', '18:00:00', 5000.00)
        """.trimIndent()
        )

        db.execSQL(
            "CREATE TABLE roles(" +
                    "RolUsu INTEGER PRIMARY KEY, " +
                    "NomRol TEXT)"
        )

        // Ingreso los datos establecidos por el sistema
        db.execSQL(
            """
        INSERT INTO roles (RolUsu, NomRol) VALUES
            (120, 'Administrador'),
            (121, 'Personal');
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
        db.execSQL(
            """
        INSERT INTO usuarios (Nombre, Pass, RolUsu, Activo) VALUES
            ('Ana', '123456', 120, 1),
            ('Juan', '123456', 120, 0),
            ('Maria', '654321', 121, 1);
        """.trimIndent()
        )

        db.execSQL(
            "CREATE TABLE socios(" +
                    "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FechaAltaSocio TEXT NOT NULL, " +
                    "FOREIGN KEY (idCliente) REFERENCES clientes(id) ON DELETE CASCADE)"
        )

        db.execSQL(
            "CREATE TABLE nosocios(" +
                    "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FechaAltaNoSocio TEXT NOT NULL, " +
                    "FOREIGN KEY (idCliente) REFERENCES clientes(id) ON DELETE CASCADE)"
        )

        db.execSQL(
            "CREATE TABLE cuotas(" +
                    "IdCuota INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idCliente INTEGER NOT NULL, " +
                    "Monto REAL NOT NULL, " +
                    "ModoPago TEXT CHECK (ModoPago IN ('Efectivo','Tarjeta')), " +
                    "Estado TEXT NOT NULL CHECK (Estado IN ('Pagada','Pendiente')), " +
                    "FechaPago TEXT, " +
                    "FechaVencimiento TEXT NOT NULL, " +
                    "CantCuotas INTEGER DEFAULT 0, " +
                    "UltDigitosTarj INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (idCliente) REFERENCES clientes(id) ON DELETE CASCADE" +
                    ")"
        )

        db.execSQL(
            "CREATE TABLE pago_actividad(" +
                    "IdPagoActividad INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idCliente INTEGER NOT NULL, " +
                    "idActividad INTEGER NOT NULL, " +
                    "FechaPago TEXT NOT NULL, " +
                    "ModoPago TEXT NOT NULL CHECK (ModoPago IN ('Efectivo','Tarjeta')) DEFAULT 'Efectivo', " +
                    "Monto REAL NOT NULL, " +
                    "Estado TEXT NOT NULL CHECK (Estado IN ('Pagada','Pendiente')) DEFAULT 'Pendiente', " +
                    "FOREIGN KEY (idCliente) REFERENCES clientes(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (idActividad) REFERENCES actividades(id) ON DELETE CASCADE" +
                    ")"
        )
        // Inserción en 'clientes'
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Ana', 'Gomez', '1995-05-15', 38123456, 'F', 'Calle Falsa 123', '555-0001', '2024-01-10', 1, 1)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Lorena', 'Lolenita', '2000-11-20', 43654321, 'M', 'Av Siempreviva 742', '555-0002', '2024-02-20', 1, 0)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Javier', 'Rodriguez', '1985-03-01', 28987654, 'M', 'Blvd de los Sueños 50', '555-0003', '2023-11-05', 0, 1)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Marcos', 'Juarez', '1993-12-13', 40256321, 'M', 'Av Luna 444', '444-1002', '2023-03-14', 1, 0)"
        )
        db.execSQL(
                "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                        "VALUES ('Nicolas', 'Mora', '1970-10-15', 20652325, 'M', 'Calle Sol 123', '333-0001', '2022-01-10', 1, 1)"
                )

        db.execSQL("INSERT INTO socios (idCliente, FechaAltaSocio) VALUES (1, '2024-01-10')")
        db.execSQL("INSERT INTO socios (idCliente, FechaAltaSocio) VALUES (3, '2023-11-05')")
        db.execSQL("INSERT INTO nosocios (idCliente, FechaAltaNoSocio) VALUES (2, '2024-02-20')")

        db.execSQL(
            "INSERT INTO cuotas (idCliente, monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj) " +
                    "VALUES (1, 5000, 'Tarjeta', 'Pagada', '2024-03-01', '2024-03-10', 3, 4567)"
        )
        // Cuota Pendiente en Efectivo
        db.execSQL(
            "INSERT INTO cuotas (idCliente, monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj) " +
                    "VALUES (3, 5000, 'Efectivo', 'Pendiente', '1900-01-01', '2024-03-10', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO cuotas (idCliente, monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj) " +
                    "VALUES (5, 8000, 'Efectivo', 'Pendiente', '1900-01-01', '2024-03-10', 0, 0)"
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

    // Insertar datos que se obtienen desde los Form (Integración de la db)
    //REgistro
    //TODO: Falta crear la primera cuota al socio
    fun insertarCliente(
        nombre: String,
        apellido: String,
        fechaNac: String,
        dni: Int,
        genero: String,
        direccion: String,
        telefono: String,
        fechaInsc: String,
        aptoFisico: Boolean,
        socio: Boolean
    ) {
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
        values.put("Socio", if (socio) 1 else 0)
        db.insert("clientes", null, values)
    }

    //Listado de clientes
    fun obtenerClientes(): List<Cliente> {
        val db = readableDatabase
        Log.d("DBHelper", "Iniciando consulta de clientes...")
        val listaClientesDB = mutableListOf<Cliente>()
        val cursor = db.rawQuery("SELECT * FROM clientes", null)
        val id = "id"
        val nombre = "Nombre"
        val apellido = "Apellido"
        val fechaNacimiento = "FechaNacimiento"
        val dni = "DNI"
        val genero = "Genero"
        val direccion = "Direccion"
        val telefono = "Telefono"
        val fechaInscripcion = "FechaInscripcion"
        val aptoFisico = "AptoFisico"
        val socio = "Socio"
        if (cursor.moveToFirst()) {
            do {
                try {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                    val dni = cursor.getInt(cursor.getColumnIndexOrThrow(dni))
                    val aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow(aptoFisico))
                    val socio = cursor.getInt(cursor.getColumnIndexOrThrow(socio))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(nombre))
                    val apellido = cursor.getString(cursor.getColumnIndexOrThrow(apellido))
                    val fechaNacimiento =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaNacimiento))
                    val genero = cursor.getString(cursor.getColumnIndexOrThrow(genero))
                    val direccion = cursor.getString(cursor.getColumnIndexOrThrow(direccion))
                    val telefono = cursor.getString(cursor.getColumnIndexOrThrow(telefono))
                    val fechaInscripcion =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaInscripcion))

                    // Crear y añadir el objeto Cliente
                    val cliente = Cliente(
                        id, nombre, apellido, fechaNacimiento, dni, genero,
                        direccion, telefono, fechaInscripcion, aptoFisico, socio
                    )
                    listaClientesDB.add(cliente)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error al mapear cliente: ${e.message}")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return listaClientesDB
    }

    //Eliminar cliente, los socios se eliminan si no tienen cuota pendiente
    companion object {
        const val RESULT_OK = 1
        const val RESULT_HAS_DEBT = 2
        const val RESULT_ERROR = 0
        private const val TAG = "DEBUG_DB"
    }

    fun verificarCuotasPendientes(idCliente: Int): Boolean {
        val db = readableDatabase
        var tieneDeudas = false

        Log.d(TAG, "Verificando cuotas pendientes para cliente ID: $idCliente")

        val query = """
        SELECT COUNT(*) 
        FROM cuotas 
        WHERE idCliente = ? AND Estado = 'Pendiente'
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idCliente.toString()))

        if (cursor.moveToFirst()) {
            val cantidadPendientes = cursor.getInt(0)
            tieneDeudas = cantidadPendientes > 0
            Log.d(TAG, "Cliente $idCliente tiene $cantidadPendientes cuotas pendientes")
        } else {
            Log.d(TAG, "No se encontraron cuotas para el cliente $idCliente")
        }

        cursor.close()

        return tieneDeudas
    }
    fun eliminarClientePorId(idCliente: Int): Int {
        val db = writableDatabase

        Log.d(TAG, "Intentando eliminar cliente con ID: $idCliente")

        val tieneDeudas = verificarCuotasPendientes(idCliente)
        if (tieneDeudas) {
            Log.d(TAG, "No se eliminó el cliente $idCliente: tiene cuotas pendientes")
            db.close()
            return RESULT_HAS_DEBT
        }

        return try {
            val filasEliminadas = db.delete("clientes", "id = ?", arrayOf(idCliente.toString()))
            Log.d(TAG, "Filas eliminadas: $filasEliminadas para cliente $idCliente")

            if (filasEliminadas > 0) {
                RESULT_OK
            } else {
                Log.d(TAG, "No se encontró cliente con ID $idCliente para eliminar")
                RESULT_ERROR
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al eliminar cliente $idCliente: ${e.message}", e)
            RESULT_ERROR
        } finally {
            db.close()
        }
    }

/*    //Diferenciar cliente como socio
    fun insertarSocio(idCliente: Int, fechaAltaSoc: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("idCliente", idCliente)
        values.put("FechaAltaSocio", fechaAltaSoc)
        db.insert("socios", null, values)
    }

    //Listado de socios
    fun obtenerSocios(): List<String> {
        val db = readableDatabase
        val lista = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM socios", null)
        if (cursor.moveToFirst()) {
            do {
                val idCliente = cursor.getInt(0)
                val fechaAlta = cursor.getString(1)
                lista.add("Cliente ID: $idCliente - Alta: $fechaAlta")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    //Diferenciar cliente como no socio
    fun insertarNoSocio(idCliente: Int, fechaAltaNoSoc: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("idCliente", idCliente)
        values.put("FechaAltaNoSocio", fechaAltaNoSoc)
        db.insert("nosocios", null, values)
    }

    //Listado de no socios
    fun obtenerNoSocios(): List<String> {
        val db = readableDatabase
        val lista = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM nosocios", null)
        if (cursor.moveToFirst()) {
            do {
                val idCliente = cursor.getInt(0)
                val fechaAlta = cursor.getString(1)
                lista.add("Cliente ID: $idCliente - Alta No Socio: $fechaAlta")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }*/

    // Nombre y Apellido de Socios para Registro Pago Socio
    fun obtenerSociosClientes(): List<Cliente> {
        val db = readableDatabase
        Log.d("DBHelper", "Iniciando consulta de clientes SOCIOS...")
        val listaClientesDB = mutableListOf<Cliente>()

        // La única diferencia es "WHERE Socio = 1"
        val cursor = db.rawQuery("SELECT * FROM clientes WHERE Socio = 1", null)

        val id = "id"
        val nombre = "Nombre"
        val apellido = "Apellido"
        val fechaNacimiento = "FechaNacimiento"
        val dni = "DNI"
        val genero = "Genero"
        val direccion = "Direccion"
        val telefono = "Telefono"
        val fechaInscripcion = "FechaInscripcion"
        val aptoFisico = "AptoFisico"
        val socio = "Socio"

        if (cursor.moveToFirst()) {
            do {
                try {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                    val dni = cursor.getInt(cursor.getColumnIndexOrThrow(dni))
                    val aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow(aptoFisico))
                    val socio = cursor.getInt(cursor.getColumnIndexOrThrow(socio))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(nombre))
                    val apellido = cursor.getString(cursor.getColumnIndexOrThrow(apellido))
                    val fechaNacimiento =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaNacimiento))
                    val genero = cursor.getString(cursor.getColumnIndexOrThrow(genero))
                    val direccion = cursor.getString(cursor.getColumnIndexOrThrow(direccion))
                    val telefono = cursor.getString(cursor.getColumnIndexOrThrow(telefono))
                    val fechaInscripcion =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaInscripcion))

                    val cliente = Cliente(
                        id, nombre, apellido, fechaNacimiento, dni, genero,
                        direccion, telefono, fechaInscripcion, aptoFisico, socio
                    )
                    listaClientesDB.add(cliente)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error al cargar cliente socio: ${e.message}")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.d("DBHelper", "Se encontraron ${listaClientesDB.size} clientes socios.")
        return listaClientesDB
    }

    // Nombre y Apellido de No Socios para Registro Pago No Socio
    fun obtenerNoSociosClientes(): List<Cliente> {
        val db = readableDatabase
        Log.d("DBHelper", "Iniciando consulta de clientes NO SOCIOS...")
        val listaClientesDB = mutableListOf<Cliente>()

        val cursor = db.rawQuery("SELECT * FROM clientes WHERE Socio = 0", null)

        val id = "id"
        val nombre = "Nombre"
        val apellido = "Apellido"
        val fechaNacimiento = "FechaNacimiento"
        val dni = "DNI"
        val genero = "Genero"
        val direccion = "Direccion"
        val telefono = "Telefono"
        val fechaInscripcion = "FechaInscripcion"
        val aptoFisico = "AptoFisico"
        val socio = "Socio"

        if (cursor.moveToFirst()) {
            do {
                try {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                    val dni = cursor.getInt(cursor.getColumnIndexOrThrow(dni))
                    val aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow(aptoFisico))
                    val socio = cursor.getInt(cursor.getColumnIndexOrThrow(socio))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow(nombre))
                    val apellido = cursor.getString(cursor.getColumnIndexOrThrow(apellido))
                    val fechaNacimiento =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaNacimiento))
                    val genero = cursor.getString(cursor.getColumnIndexOrThrow(genero))
                    val direccion = cursor.getString(cursor.getColumnIndexOrThrow(direccion))
                    val telefono = cursor.getString(cursor.getColumnIndexOrThrow(telefono))
                    val fechaInscripcion =
                        cursor.getString(cursor.getColumnIndexOrThrow(fechaInscripcion))

                    val cliente = Cliente(
                        id, nombre, apellido, fechaNacimiento, dni, genero,
                        direccion, telefono, fechaInscripcion, aptoFisico, socio
                    )
                    listaClientesDB.add(cliente)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error al obtener cliente: ${e.message}")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.d("DBHelper", "Se encontraron ${listaClientesDB.size} clientes no socios.")
        return listaClientesDB
    }


    // Obtener cuotas pendientes de un cliente
    @SuppressLint("Range")
    fun obtenerCuotasPendientes(idCliente: Int): List<Cuotas> {
        val listaCuotas = mutableListOf<Cuotas>()
        val db = readableDatabase
        Log.d("DBHelper", "Buscando cuotas pendientes para cliente ID: $idCliente")

        // Consulta filtrada por idCliente y Estado='Pendiente'
        val query = "SELECT * FROM cuotas WHERE idCliente = ? AND Estado = 'Pendiente' ORDER BY FechaVencimiento ASC"
        val cursor = db.rawQuery(query, arrayOf(idCliente.toString()))

        if (cursor.moveToFirst()) {
            do {
                try {
                    // (IdCuota, idCliente, Monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj)

                    val idCuota = cursor.getInt(cursor.getColumnIndexOrThrow("IdCuota"))
                    val idClienteDB = cursor.getInt(cursor.getColumnIndexOrThrow("idCliente"))
                    val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("Monto"))
                    val modoPago = cursor.getString(cursor.getColumnIndexOrThrow("ModoPago"))
                    val estado = cursor.getString(cursor.getColumnIndexOrThrow("Estado"))
                    val fechaPago = cursor.getString(cursor.getColumnIndexOrThrow("FechaPago"))
                    val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("FechaVencimiento"))
                    val cantCuotas = cursor.getInt(cursor.getColumnIndexOrThrow("CantCuotas"))
                    val ultDigitosTarj = cursor.getInt(cursor.getColumnIndexOrThrow("UltDigitosTarj"))

                    val cuota = Cuotas(
                        idCuota, idClienteDB, monto, modoPago, estado,
                        fechaPago, fechaVencimiento, cantCuotas, ultDigitosTarj
                    )
                    listaCuotas.add(cuota)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Error al mapear cuota: ${e.message}")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        Log.d("DBHelper", "Se encontraron ${listaCuotas.size} cuotas pendientes.")
        return listaCuotas
    }

    //Registrar pAgo de socios
    //TODO: Falta crear próxima cuota (vencimiento un mes después de la fecha de pago)
    fun insertarCuota(
        idCliente: Int,
        monto: Double,
        modoPago: String,
        estado: String,
        fechaPago: String,
        fechaVencto: String,
        cantCuotas: String,
        ultDigitosTarj: String
    ) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("idCliente", idCliente)
        values.put("Monto", monto)
        values.put("ModoPago", modoPago)
        values.put("Estado", estado)
        values.put("FechaPago", fechaPago)
        values.put("FechaVencimiento", fechaVencto)
        values.put("CantCuotas", cantCuotas)
        values.put("UltDigitosTarj", ultDigitosTarj)
        db.insert("cuotas", null, values)
    }

    //Listado de cuotas
    fun obtenerCuotas(): List<Cuotas> {
        val db = readableDatabase
        val lista = mutableListOf<Cuotas>()
        val cursor = db.rawQuery("SELECT * FROM cuotas", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Cuotas(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    //Pago no socios
    fun insertarPagoActividad(
        idCliente: Int,
        idActividad: Int,
        fechaPago: String,
        modoPago: String,
        monto: Double,
        estado: String
    ) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("idCliente", idCliente)
        values.put("idActividad", idActividad)
        values.put("FechaPago", fechaPago)
        values.put("ModoPago", modoPago)
        values.put("Monto", monto)
        values.put("Estado", estado)
        db.insert("pago_actividad", null, values)
    }

    //Listado de pagos actividad
    fun obtenerPagoActividad(): List<PagoActividad> {
        val db = readableDatabase
        val lista = mutableListOf<PagoActividad>()
        val cursor = db.rawQuery("SELECT * FROM pago_actividad", null)
        if (cursor.moveToFirst()) {
            do {
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
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }


    //Listado Actividades Registro Pago NO socio
    @SuppressLint("Range")
    fun obtenerActividades(): List<Actividad> {
        val actividadesList = mutableListOf<Actividad>()
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(
                "SELECT id, Nombre, DiaSemana, Hora, Monto FROM actividades ORDER BY Nombre ASC",
                null
            )

        } catch (e: Exception) {
            Log.e("DBHelper", "Error al leer actividades", e)
            db.close()
            return actividadesList
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val nombre = cursor.getString(cursor.getColumnIndex("Nombre"))
                val diaSemana = cursor.getString(cursor.getColumnIndex("DiaSemana"))
                val hora = cursor.getString(cursor.getColumnIndex("Hora"))
                val monto = cursor.getDouble(cursor.getColumnIndex("Monto"))

                val actividad = Actividad(id, nombre, diaSemana, hora, monto)
                actividadesList.add(actividad)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return actividadesList
    }

    //Editar cliente
    fun actualizarCliente(
        idCliente: Int,
        nombre: String,
        apellido: String,
        fechaNac: String,
        dni: Int,
        genero: String,
        direccion: String,
        telefono: String,
        fechaInsc: String,
        aptoFisico: Boolean,
        socio: Boolean
    ): Boolean {
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
        values.put("Socio", if (socio) 1 else 0)

        val filasAfectadas = db.update("clientes", values, "id = ?", arrayOf(idCliente.toString()))
        db.close()

        return filasAfectadas > 0
    }

    //TODO: Listado de clientes a los que se les vence la cuota (morosos)
}