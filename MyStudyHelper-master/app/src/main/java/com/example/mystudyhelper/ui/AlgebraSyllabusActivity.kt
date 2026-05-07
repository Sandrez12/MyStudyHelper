package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class AlgebraSyllabusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algebra_syllabus)

        findViewById<ImageView>(R.id.btnRegresar).setOnClickListener {
            finish()
        }

        // Botón del Escáner Photomath
        findViewById<CardView>(R.id.cardEscaner).setOnClickListener {
            Toast.makeText(this, "Abriendo cámara para escanear...", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, ScannerActivity::class.java)
            // startActivity(intent)
        }

        // Botón de la Lección tipo Duolingo
        findViewById<CardView>(R.id.cardLeccion1).setOnClickListener {
            Toast.makeText(this, "Iniciando cuestionario gamificado...", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, QuizActivity::class.java)
            // startActivity(intent)
        }
    }
}