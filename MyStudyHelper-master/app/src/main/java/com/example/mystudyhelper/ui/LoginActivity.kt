package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.mystudyhelper.R
import com.example.mystudyhelper.model.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Conectamos con el diseño visual que creamos (activity_login.xml)
        setContentView(R.layout.activity_login)

        // 1. Inicializamos la Base de Datos
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "study_database"
        ).build()

        // 2. Conectamos los elementos de la pantalla
        val etCorreo = findViewById<EditText>(R.id.etLoginCorreo)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // 3. Lógica al presionar el botón
        btnLogin.setOnClickListener {
            val email = etCorreo.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            // Validamos que no estén vacíos
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Usamos una Corrutina para buscar en la base de datos sin trabar la pantalla
                lifecycleScope.launch {
                    val user = db.userDao().login(email, pass)

                    if (user != null) {
                        // ¡Existe! Lo mandamos a la pantalla de Bienvenida
                        val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                        // Le mandamos su nombre en la mochila para que lo salude
                        intent.putExtra("NOMBRE_USUARIO", user.nombre)
                        startActivity(intent)
                        finish() // Cerramos el login
                    } else {
                        // No existe o escribió mal la contraseña
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas o usuario no registrado", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}