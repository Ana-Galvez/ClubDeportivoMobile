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
import com.example.clubdeportivomovile.Moroso

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SportifyClub.db", null, 2) {

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
                    "Genero TEXT NOT NULL CHECK (Genero IN ('F', 'M', 'Prefiero no decirlo')), " +
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
            ('Natación', 'Lunes', '11:00', 5000.00),
            ('Pilates', 'Lunes', '18:00', 8000.00),
            ('Tenis', 'Miércoles', '18:00', 10000.00),
            ('Musculación', 'Martes', '19:00', 5000.00),
            ('Yoga', 'Lunes', '16:00', 5000.00),
            ('Aerobic', 'Martes', '18:00', 5000.00)
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
            ('Maria', '123456', 120, 0),
            ('Jack', '654321', 121, 1);
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
                    "Monto REAL NOT NULL, " +
                    "Estado TEXT NOT NULL CHECK (Estado IN ('Pagada','Pendiente')) DEFAULT 'Pendiente', " +
                    "FOREIGN KEY (idCliente) REFERENCES clientes(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (idActividad) REFERENCES actividades(id) ON DELETE CASCADE" +
                    ")"
        )
        // Inserción en 'clientes'
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Ana', 'Gomez', '1995-05-15', 38123456, 'F', 'Calle Falsa 123', '05550001', '2024-01-10', 1, 1)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Lorena', 'Lolenita', '2000-11-20', 43654321, 'M', 'Av Siempreviva 742', '05550002', '2024-02-20', 1, 0)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Javier', 'Rodriguez', '1985-03-01', 28987654, 'M', 'Blvd de los Sueños 50', '05550003', '2023-11-05', 0, 1)"
        )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Marcos', 'Juarez', '1993-12-13', 40256321, 'M', 'Av Luna 444', '04441002', '2023-03-14', 1, 0)"
        )
        db.execSQL(
                "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                        "VALUES ('Nicolas', 'Mora', '1970-10-15', 20652325, 'M', 'Calle Sol 123', '03330001', '2022-01-10', 1, 1)"
                )
        db.execSQL(
            "INSERT INTO clientes (Nombre, Apellido, FechaNacimiento, DNI, Genero, Direccion, Telefono, FechaInscripcion, AptoFisico, Socio) " +
                    "VALUES ('Mariana', 'Luque', '1993-10-15', 20659696, 'F', 'Calle Solari 123', '03339930', '2023-01-10', 1, 1)"
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
                    "VALUES (3, 5000, 'Efectivo', 'Pendiente', null, '2025-11-12', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO cuotas (idCliente, monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj) " +
                    "VALUES (5, 8000, 'Efectivo', 'Pendiente', null, '2025-11-13', 0, 0)"
        )
        db.execSQL(
            "INSERT INTO cuotas (idCliente, monto, ModoPago, Estado, FechaPago, FechaVencimiento, CantCuotas, UltDigitosTarj) " +
                    "VALUES (6, 8000, 'Efectivo', 'Pendiente', null, '2025-11-13', 0, 0)"
        )
    }

   override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
       // Desactivar foreign keys durante el DROP
       db.execSQL("PRAGMA foreign_keys=OFF")
       db.beginTransaction()
       try {
           // Primero las tablas hijas (que dependen de otras)
           db.execSQL("DROP TABLE IF EXISTS pago_actividad")
           db.execSQL("DROP TABLE IF EXISTS cuotas")
           db.execSQL("DROP TABLE IF EXISTS socios")
           db.execSQL("DROP TABLE IF EXISTS nosocios")
           db.execSQL("DROP TABLE IF EXISTS usuarios")
           db.execSQL("DROP TABLE IF EXISTS roles")
           db.execSQL("DROP TABLE IF EXISTS actividades")
           db.execSQL("DROP TABLE IF EXISTS clientes")

           db.setTransactionSuccessful()
       } finally {
           db.endTransaction()
           db.execSQL("PRAGMA foreign_keys=ON")
       }

       onCreate(db)
   }


    // Insertar datos que se obtienen desde los Form (Integración de la db)
    //Registro
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

        // Insertar cliente y se recupera por el ID
        val idCliente = db.insert("clientes", null, values)

        // Si es socio, la primera cuota se crea automáticamente
        if (socio && idCliente != -1L) {
            crearPrimerCuota(db, idCliente, 5000.0)
        }
        db.close()
    }

    fun crearPrimerCuota(db: SQLiteDatabase, idCliente: Long, monto: Double) {
        try {
            // Calcular la fecha de vencimiento = hoy + 1 mes
            val fechaVencimiento = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.LocalDate.now().plusMonths(1).toString()
            } else {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                val cal = java.util.Calendar.getInstance()
                cal.add(java.util.Calendar.MONTH, 1)
                sdf.format(cal.time)
            }

            // Crear los valores de la cuota
            val values = ContentValues()
            values.put("idCliente", idCliente)
            values.put("Monto", monto)
            values.put("Estado", "Pendiente")
            values.putNull("ModoPago")
            values.putNull("FechaPago")
            values.put("FechaVencimiento", fechaVencimiento)
            values.put("CantCuotas", 0)
            values.put("UltDigitosTarj", 0)

            db.insert("cuotas", null, values)
            Log.d("DBHelper", "Primera cuota creada correctamente para el cliente con ID $idCliente")

        } catch (e: Exception) {
            Log.e("DBHelper", "Error al crear la primera cuota del cliente con ID $idCliente : ${e.message}")
        }
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

    //Registrar pago de socios
    //Lo hace por medio de una transacción
    fun actualizarCuotaPagada(
        db: SQLiteDatabase,
        idCuota: Int,
        fechaPago: String,
        modoPago: String,
        cantCuotas: String,
        ultDigitosTarj: String
    ) {
        val values = ContentValues()
        values.put("Estado", "Pagada")
        values.put("FechaPago", fechaPago)
        values.put("ModoPago", modoPago)

        // Convierte el string del spinner a entero para poder guardarlo
        // Si no es un número (ej: "Seleccionar..."), guarda 0
        val cuotasInt = cantCuotas.filter { it.isDigit() }.toIntOrNull() ?: 0
        values.put("CantCuotas", cuotasInt)

        // Si ultDigitosTarj está vacío, guarda 0
        val digitosInt = ultDigitosTarj.toIntOrNull() ?: 0
        values.put("UltDigitosTarj", digitosInt)

        // Actualiza la fila
        val filas = db.update("cuotas", values, "IdCuota = ?", arrayOf(idCuota.toString()))

        if (filas == 0) {
            throw Exception("Error: No se encontró la cuota $idCuota para actualizar.")
        }
        Log.d("DBHelper", "Cuota $idCuota actualizada a 'Pagada'.")
    }

    fun crearSiguienteCuota(
        db: SQLiteDatabase,
        idCliente: Int,
        monto: Double,
        fechaVencAnterior: String
    ) {
        try {
            // Calcula la fecha de vencimiento = fechaVencAnterior + 30 días
            val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val cal = java.util.Calendar.getInstance()
            cal.time = parser.parse(fechaVencAnterior)
            cal.add(java.util.Calendar.DAY_OF_YEAR, 30)
            val fechaVencimiento = parser.format(cal.time)

            // Crear los valores de la nueva cuota
            val values = ContentValues()
            values.put("idCliente", idCliente)
            values.put("Monto", monto)
            values.put("Estado", "Pendiente")
            values.putNull("ModoPago")
            values.putNull("FechaPago")
            values.put("FechaVencimiento", fechaVencimiento)
            values.put("CantCuotas", 0)
            values.put("UltDigitosTarj", 0)

            db.insertOrThrow("cuotas", null, values)
            Log.d("DBHelper", "Siguiente cuota creada para cliente $idCliente con vencimiento $fechaVencimiento")

        } catch (e: java.text.ParseException) {
            Log.e("DBHelper", "Error al parsear la fecha de vencimiento anterior $fechaVencAnterior", e)
            throw e
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al crear la siguiente cuota", e)
            throw e
        }
    }


    //Pago no socios
    fun insertarPagoActividad(
        idCliente: Int,
        idActividad: Int,
        fechaPago: String,
        monto: Double,
        estado: String
    ) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("idCliente", idCliente)
        values.put("idActividad", idActividad)
        values.put("FechaPago", fechaPago)
        values.put("Monto", monto)
        values.put("Estado", estado)
        db.insert("pago_actividad", null, values)
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
        aptoFisico: Int,
        socio: Int
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
        values.put("AptoFisico", aptoFisico)
        values.put("Socio", socio)

        val filasAfectadas = db.update("clientes", values, "id = ?", arrayOf(idCliente.toString()))
        db.close()

        return filasAfectadas > 0
    }

    fun obtenerMorososDeHoy(): MutableList<Moroso> {
        val hoy = try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.LocalDate.now().toString() // yyyy-MM-dd
            } else {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                sdf.format(java.util.Date())
            }
        } catch (_: Exception) {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            sdf.format(java.util.Date())
        }

        val lista = mutableListOf<Moroso>()
        val db = readableDatabase

        val sql = """
        SELECT  c.id,
                c.Nombre,
                c.Apellido,
                c.DNI,
                c.Telefono,
                SUM(q.Monto) AS TotalAdeudado
        FROM clientes c
        JOIN cuotas q ON q.idCliente = c.id
        WHERE q.Estado = 'Pendiente'
          AND q.FechaVencimiento = ?
        GROUP BY c.id, c.Nombre, c.Apellido, c.DNI, c.Telefono
        HAVING TotalAdeudado > 0
        ORDER BY c.Apellido COLLATE NOCASE ASC, c.Nombre COLLATE NOCASE ASC
    """.trimIndent()

        val cur = db.rawQuery(sql, arrayOf(hoy))
        Log.d("DBHelper", "MorososDeHoy — hoy=$hoy, rows=${cur.count}")

        if (cur.moveToFirst()) {
            val idxId  = cur.getColumnIndexOrThrow("id")
            val idxNom = cur.getColumnIndexOrThrow("Nombre")
            val idxApe = cur.getColumnIndexOrThrow("Apellido")
            val idxDni = cur.getColumnIndexOrThrow("DNI")
            val idxTel = cur.getColumnIndexOrThrow("Telefono")
            val idxTot = cur.getColumnIndexOrThrow("TotalAdeudado")

            do {
                val id = cur.getInt(idxId)
                val nombre = "${cur.getString(idxNom)} ${cur.getString(idxApe)}".trim()
                val dni = cur.getInt(idxDni)
                val tel = cur.getString(idxTel) ?: "-"
                val total = cur.getDouble(idxTot)

                lista.add(Moroso(id, nombre, dni, tel, total))
            } while (cur.moveToNext())
        }

        cur.close()
        db.close()
        return lista
    }

    // Validar usuario y contraseña
    fun validarUsuario(usuario: String, pass: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE Nombre = ? AND Pass = ? AND Activo = 1",
            arrayOf(usuario, pass)
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
}
