package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class EnglishLevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_english_levels)

        findViewById<ImageView>(R.id.btnRegresarEnglish).setOnClickListener { finish() }

        // Ahora vinculamos los niveles usando LinearLayout
        findViewById<LinearLayout>(R.id.cardA1).setOnClickListener { abrirTemario("A1") }
        findViewById<LinearLayout>(R.id.cardA2).setOnClickListener { abrirTemario("A2") }
        findViewById<LinearLayout>(R.id.cardB1).setOnClickListener { abrirTemario("B1") }
        findViewById<LinearLayout>(R.id.cardB2).setOnClickListener { abrirTemario("B2") }
        findViewById<LinearLayout>(R.id.cardC1).setOnClickListener { abrirTemario("C1") }

        // ==========================================
        // BOTONES INFERIORES (Con sus nuevos IDs)
        // ==========================================

        // Botón Estudiar (Azul)
        findViewById<CardView>(R.id.btnNavEstudiar).setOnClickListener {
            Toast.makeText(this, "👆 Selecciona un nivel de la lista de arriba para ver temas", Toast.LENGTH_LONG).show()
        }

        // Botón Practicar (Naranja)
        findViewById<CardView>(R.id.btnNavPracticar).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("MATERIA", "English")
            startActivity(intent)
        }
    }

    private fun abrirTemario(nivel: String) {
        val intent = Intent(this, LessonListActivity::class.java)
        intent.putExtra("MATERIA", nivel)
        startActivity(intent)
    }
}