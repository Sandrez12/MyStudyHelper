package com.example.mystudyhelper.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class SubjectMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_menu)

        val materiaPrincipal = intent.getStringExtra("MATERIA") ?: "Materia"

        // Título dinámico: Si es Inglés, lo cambia automáticamente para que se vea más natural
        val tituloAjustado = if (materiaPrincipal == "Inglés") "English Levels" else materiaPrincipal
        findViewById<TextView>(R.id.tvTituloMateriaDinamico).text = tituloAjustado

        findViewById<ImageView>(R.id.btnRegresarMenu).setOnClickListener { finish() }

        // ==========================================
        // 1. CARGAR BARRAS DE PROGRESO DE INMEDIATO
        // ==========================================
        val contenedor = findViewById<LinearLayout>(R.id.contenedorBarrasProgreso)

        val listaSubmaterias = when (materiaPrincipal) {
            "Inglés" -> listOf(
                Pair("A1", 75), Pair("A2", 20), Pair("B1", 0), Pair("B2", 0), Pair("C1", 0)
            )
            "Programación" -> listOf(
                Pair("Programación Orientada a Objetos", 75), Pair("Arquitectura Cliente-Servidor", 40)
            )
            "Graficación" -> listOf(
                Pair("Rasterización vs Vectoriales", 60), Pair("Transformaciones 3D", 20)
            )
            "Sistemas Embebidos" -> listOf(
                Pair("Microcontroladores e IoT", 90), Pair("Sensores y Actuadores", 30)
            )
            else -> listOf(Pair("Fundamentos", 0))
        }

        // Dibujamos las tarjetas visuales
        for (submateria in listaSubmaterias) {
            val nombreSub = submateria.first
            val progresoSub = submateria.second

            // Formato especial para Inglés (Level A1 - Beginner, etc.)
            val tituloMostrar = if (materiaPrincipal == "Inglés") {
                when(nombreSub) {
                    "A1" -> "Level A1 - Beginner"
                    "A2" -> "Level A2 - Elementary"
                    "B1" -> "Level B1 - Intermediate"
                    "B2" -> "Level B2 - Upper-Int"
                    "C1" -> "Level C1 - Advanced"
                    else -> nombreSub
                }
            } else {
                nombreSub
            }

            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 32, 0, 32)

                // Efecto visual al tocar la pantalla (Ripple effect) corregido
                val outValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                setBackgroundResource(outValue.resourceId)

                isClickable = true
                isFocusable = true

                // Al tocar la barra, abre las lecciones de ESE nivel
                setOnClickListener {
                    val intent = Intent(this@SubjectMenuActivity, LessonListActivity::class.java)
                    intent.putExtra("MATERIA", nombreSub) // Le pasa "A1" o "Programación..."
                    startActivity(intent)
                }
            }

            val headerLayout = RelativeLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            val tvTitulo = TextView(this).apply {
                text = "📖 $tituloMostrar"
                textSize = 18f
                setTextColor(Color.parseColor("#333333"))
                setTypeface(null, Typeface.BOLD)
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply { addRule(RelativeLayout.ALIGN_PARENT_START) }
            }

            val tvProgreso = TextView(this).apply {
                text = "$progresoSub%"
                textSize = 14f
                setTextColor(Color.parseColor("#666666"))
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply { addRule(RelativeLayout.ALIGN_PARENT_END) }
            }

            headerLayout.addView(tvTitulo)
            headerLayout.addView(tvProgreso)

            val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12).apply { setMargins(0, 16, 0, 0) }
                progress = progresoSub
                val colorHex = if (progresoSub > 60) "#FF5252" else if (progresoSub > 10) "#FFD740" else "#BDBDBD"
                progressTintList = android.content.res.ColorStateList.valueOf(Color.parseColor(colorHex))
            }

            itemLayout.addView(headerLayout)
            itemLayout.addView(progressBar)
            contenedor.addView(itemLayout)
        }

        // ==========================================
        // 2. BOTONES INFERIORES
        // ==========================================

        // El botón azul ya no necesita navegar, porque los temas ya están en pantalla
        findViewById<CardView>(R.id.btnMenuEstudiarGen).setOnClickListener {
            Toast.makeText(this, "👆 Toca uno de los niveles de arriba para ver las lecciones", Toast.LENGTH_SHORT).show()
        }

        findViewById<CardView>(R.id.btnMenuPracticarGen).setOnClickListener {
            val intentQuiz = Intent(this, QuizActivity::class.java)
            if (materiaPrincipal == "Inglés") intentQuiz.putExtra("MATERIA", "English")
            else intentQuiz.putExtra("MATERIA", materiaPrincipal)
            startActivity(intentQuiz)
        }
    }
}