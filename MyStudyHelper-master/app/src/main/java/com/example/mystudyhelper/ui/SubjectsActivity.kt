package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class SubjectsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)

        // ==========================================
        // 1. MATEMÁTICAS (Va a su propio menú avanzado)
        // ==========================================
        findViewById<CardView>(R.id.cardMatematicas).setOnClickListener {
            val intent = Intent(this, MathMenuActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // 2. INGLÉS (Va a la pantalla de niveles A1-C1)
        // ==========================================
        findViewById<CardView>(R.id.cardDashboardIngles).setOnClickListener {
            // Mandamos a un menú intermedio como en Programación
            val intent = Intent(this, SubjectMenuActivity::class.java)
            intent.putExtra("MATERIA", "Inglés")
            startActivity(intent)
        }

        // ==========================================
        // 3. MATERIAS UNIVERSITARIAS (Van al Menú Genérico)
        // ==========================================
        findViewById<CardView>(R.id.cardDashboardProgramacion).setOnClickListener {
            val intent = Intent(this, SubjectMenuActivity::class.java)
            intent.putExtra("MATERIA", "Programación")
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardDashboardGraficacion).setOnClickListener {
            val intent = Intent(this, SubjectMenuActivity::class.java)
            intent.putExtra("MATERIA", "Graficación")
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardDashboardEmbebidos).setOnClickListener {
            val intent = Intent(this, SubjectMenuActivity::class.java)
            intent.putExtra("MATERIA", "Sistemas Embebidos")
            startActivity(intent)
        }
    }
}