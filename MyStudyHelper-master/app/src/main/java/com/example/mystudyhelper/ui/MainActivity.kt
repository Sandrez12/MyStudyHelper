package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R
import com.example.mystudyhelper.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    // Instanciamos el ViewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buscamos el botón en el diseño XML
        val btnExit = findViewById<Button>(R.id.btnExit)

        // 1. Ante una interacción del usuario, la Vista notifica al ViewModel
        btnExit.setOnClickListener {
            viewModel.registerExitAttempt()
            Toast.makeText(this, "Intento de salida registrado...", Toast.LENGTH_SHORT).show()
        }

        // 2. El Enlace (Binding): Observamos el estado de estrés
        viewModel.showStressOptions.observe(this) { isStressed ->
            if (isStressed) {
                // Mostramos la pantalla/mensaje dando a escoger material o descanso
                Toast.makeText(this, "⚠️ Pareces estresado. ¿Quieres ver material o tomar un descanso?", Toast.LENGTH_LONG).show()
            }
        }
    }
}