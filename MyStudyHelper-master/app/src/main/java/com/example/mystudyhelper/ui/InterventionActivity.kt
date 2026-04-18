package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class InterventionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intervention) // Conecta con el diseño visual de tarjetas

        // Botón Material (Morado)
        val cardMaterial = findViewById<CardView>(R.id.cardMaterial)
        cardMaterial.setOnClickListener {
            Toast.makeText(this, "Abriendo guías de estudio...", Toast.LENGTH_SHORT).show()
        }

        // Botón Descanso (Azul)
        val cardDescanso = findViewById<CardView>(R.id.cardDescanso)
        cardDescanso.setOnClickListener {
            Toast.makeText(this, "Iniciando temporizador de 5 minutos...", Toast.LENGTH_SHORT).show()
        }

        // Botón Volver al Inicio
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Destruye esta pantalla para regresar a la app principal
        }
    }
}