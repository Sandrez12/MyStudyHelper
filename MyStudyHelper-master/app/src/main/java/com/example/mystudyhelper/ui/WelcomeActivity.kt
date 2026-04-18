package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView // Importante agregar este
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // 1. Abrimos la "mochila" y sacamos el dato. Si no viene nada, usamos "Estudiante"
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO") ?: "Estudiante"

        // 2. Buscamos el texto en la pantalla
        val tvMensaje = findViewById<TextView>(R.id.tvMensajePersonalizado)

        // 3. Actualizamos el texto combinando tu mensaje con el nombre real
        tvMensaje.text = "¡Todo listo, $nombreUsuario!\n\nTu perfil ha sido configurado. Ahora podemos ayudarte a gestionar tu tiempo y reducir el estrés."

        // Configuración del botón
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)
        btnComenzar.setOnClickListener {
            Toast.makeText(this, "Iniciando sesión de $nombreUsuario...", Toast.LENGTH_SHORT).show()
        }
    }
}