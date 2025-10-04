package com.example.clubdeportivomovile

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {

    private lateinit var contraseñaEditText: EditText
    private lateinit var toggleImageView: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        contraseñaEditText = findViewById(R.id.Contraseña)
        toggleImageView=findViewById(R.id.ivToggle)
        btnLogin.setOnClickListener {
            val intent = Intent (this, Home::class.java)
            startActivity(intent)
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


