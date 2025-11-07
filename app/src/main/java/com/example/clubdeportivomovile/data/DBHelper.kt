package com.example.clubdeportivomovile.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SportifyClub.db", null, 1) {
    //Tablas
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

        db.execSQL(
            "CREATE TABLE roles(" +
                    "RolUsu INTEGER PRIMARY KEY, " +
                    "NomRol TEXT)"
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clientes")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS roles")
        onCreate(db)
    }
}