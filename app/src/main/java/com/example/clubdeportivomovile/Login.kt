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

class Login : AppCompatActivity() {

    private lateinit var usuarioEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var toggleImageView: ImageView
    private var isPasswordVisible = false

    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        usuarioEditText = findViewById(R.id.Usuario)
        contraseñaEditText = findViewById(R.id.Contraseña)
        toggleImageView=findViewById(R.id.ivToggle)
        checkBox = findViewById(R.id.checkBox)
        btnLogin.setOnClickListener {
            val usuario = usuarioEditText.text.toString()
            val pass = contraseñaEditText.text.toString()

            if (usuario.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
            else if (!checkBox.isChecked) {
                Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            }
            else if (usuario != "admin" && pass == "1234") {
                Toast.makeText(this, "Ingresa un nombre de usuario válido", Toast.LENGTH_SHORT).show()
            }
            else if (usuario == "admin" && pass != "1234") {
                Toast.makeText(this, "Contraseña incorrecta. Intenta nuevamente", Toast.LENGTH_SHORT).show()
            }
            else if (usuario != "admin" && pass != "1234"){
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
            else if (usuario == "admin" && pass == "1234") {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Home::class.java)
                intent.putExtra("usuario",usuario)
                startActivity(intent)
            }
        }

        toggleImageView.setOnClickListener {
            if(isPasswordVisible){
                contraseñaEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                toggleImageView.setImageResource(R.drawable.ic_visibility_off)
                isPasswordVisible = false
            } else {
                contraseñaEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                toggleImageView.setImageResource(R.drawable.ic_visibility_on)
                isPasswordVisible = true
            }
            contraseñaEditText.setSelection(contraseñaEditText.text?.length ?: 0)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


