package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.CheckBox
import com.example.clubdeportivomovile.data.DBHelper

class Login : AppCompatActivity() {

    private lateinit var usuarioEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var toggleImageView: ImageView
    private var isPasswordVisible = false
    private lateinit var checkBox: CheckBox

    private val db by lazy { DBHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        usuarioEditText = findViewById(R.id.Usuario)
        contraseñaEditText = findViewById(R.id.Contraseña)
        toggleImageView = findViewById(R.id.ivToggle)
        checkBox = findViewById(R.id.checkBox)

        btnLogin.setOnClickListener {
            val usuario = usuarioEditText.text.toString().trim()
            val pass = contraseñaEditText.text.toString().trim()

            // 1️⃣ Validación campos vacíos
            if (usuario.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkBox.isChecked) {
                Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioExiste = existeUsuario(usuario)
            if (!usuarioExiste) {
                // 2️⃣ Usuario incorrecto
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contraseñaValida = validarContraseña(usuario, pass)
            if (!contraseñaValida) {
                // 3️⃣ Contraseña incorrecta
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login exitoso
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Home::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
            finish()
        }

        toggleImageView.setOnClickListener {
            if (isPasswordVisible) {
                contraseñaEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                toggleImageView.setImageResource(R.drawable.ic_visibility_off)
            } else {
                contraseñaEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                toggleImageView.setImageResource(R.drawable.ic_visibility_on)
            }
            isPasswordVisible = !isPasswordVisible
            contraseñaEditText.setSelection(contraseñaEditText.text?.length ?: 0)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Revisa si existe el usuario
    private fun existeUsuario(usuario: String): Boolean {
        val dbRead = db.readableDatabase
        val query = "SELECT * FROM usuarios WHERE Nombre = ? AND Activo = 1"
        val cursor = dbRead.rawQuery(query, arrayOf(usuario))
        val existe = cursor.count > 0
        cursor.close()
        dbRead.close()
        return existe
    }

    // Valida la contraseña del usuario
    private fun validarContraseña(usuario: String, pass: String): Boolean {
        val dbRead = db.readableDatabase
        val query = "SELECT * FROM usuarios WHERE Nombre = ? AND Pass = ? AND Activo = 1"
        val cursor = dbRead.rawQuery(query, arrayOf(usuario, pass))
        val valida = cursor.count > 0
        cursor.close()
        dbRead.close()
        return valida
    }
}

