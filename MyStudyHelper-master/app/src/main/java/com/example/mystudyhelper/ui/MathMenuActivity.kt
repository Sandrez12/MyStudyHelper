package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class MathMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math_menu)

        // ==========================================
        // FLUJO HACIA EL CUESTIONARIO (Botón Practicar)
        // ==========================================
        val btnPracticar = findViewById<CardView>(R.id.btnMenuPracticar)
        btnPracticar.setOnClickListener {
            val intent = android.content.Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        // Botón para regresar al dashboard general
        val btnRegresar = findViewById<ImageView>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish() // Cierra esta pantalla y te devuelve a la anterior
        }

        // Detectar el clic en la tarjeta de Álgebra
        val cardAlgebra = findViewById<CardView>(R.id.cardModuloAlgebra)
        cardAlgebra.setOnClickListener {
            // Lanzamos el nuevo temario de Álgebra
            val intent = android.content.Intent(this, AlgebraSyllabusActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // FLUJO HACIA EL ESCÁNER (Tarjeta Morada)
        // ==========================================
        val btnEscaner = findViewById<CardView>(R.id.cardMenuEscaner)
        btnEscaner.setOnClickListener {
            val intent = android.content.Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // FLUJO HACIA LA LISTA DE TEMAS (Botón Estudiar)
        // ==========================================
        val btnEstudiar = findViewById<CardView>(R.id.btnMenuEstudiar)
        btnEstudiar.setOnClickListener {
            // Ahora viaja a SubmateriasActivity
            val intent = android.content.Intent(this, SubmateriasActivity::class.java)
            startActivity(intent)
        }
    }
}