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

class MathMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math_menu)

        findViewById<ImageView>(R.id.btnRegresarMath).setOnClickListener { finish() }

        // Configuración del Escáner
        findViewById<CardView>(R.id.btnAbrirScanner).setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

        // ==========================================
        // DIBUJAR TEMAS DE MATEMÁTICAS CON PROGRESO
        // ==========================================
        val contenedor = findViewById<LinearLayout>(R.id.contenedorProgresoMath)

        val listaTemasMath = listOf(
            Pair("Aritmética", 85),
            Pair("Álgebra", 40),
            Pair("Geometría", 15),
            Pair("Cálculo Diferencial", 0)
        )

        for (tema in listaTemasMath) {
            val nombreTema = tema.first
            val progreso = tema.second

            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 32, 0, 32)

                val outValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                setBackgroundResource(outValue.resourceId)

                isClickable = true
                isFocusable = true

                setOnClickListener {
                    val intent = Intent(this@MathMenuActivity, LessonListActivity::class.java)
                    intent.putExtra("MATERIA", nombreTema)
                    startActivity(intent)
                }
            }

            val header = RelativeLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            val tvTitulo = TextView(this).apply {
                text = "📐 $nombreTema"
                textSize = 18f
                setTextColor(Color.parseColor("#333333"))
                setTypeface(null, Typeface.BOLD)
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply { addRule(RelativeLayout.ALIGN_PARENT_START) }
            }

            val tvPorcentaje = TextView(this).apply {
                text = "$progreso%"
                textSize = 14f
                setTextColor(Color.parseColor("#666666"))
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply { addRule(RelativeLayout.ALIGN_PARENT_END) }
            }

            header.addView(tvTitulo)
            header.addView(tvPorcentaje)

            val bar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12).apply { setMargins(0, 16, 0, 0) }
                progress = progreso
                val colorHex = if (progreso > 60) "#58CC02" else if (progreso > 10) "#FF9800" else "#BDBDBD"
                progressTintList = android.content.res.ColorStateList.valueOf(Color.parseColor(colorHex))
            }

            itemLayout.addView(header)
            itemLayout.addView(bar)
            contenedor.addView(itemLayout)
        }

        // ==========================================
        // BOTONES INFERIORES
        // ==========================================

        // Botón Estudiar (Azul)
        findViewById<CardView>(R.id.btnMenuEstudiarMath).setOnClickListener {
            Toast.makeText(this, "👆 Toca uno de los temas de arriba para repasar", Toast.LENGTH_SHORT).show()
        }

        // Botón Practicar (Naranja) -> Abre el Quiz de Matemáticas
        findViewById<CardView>(R.id.btnMenuPracticarMath).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            // Mandamos una etiqueta que pueda reconocer nuestro QuizActivity
            intent.putExtra("MATERIA", "Álgebra") // Puedes cambiarlo a "Aritmética" o "Matemáticas" según cómo lo configuraste en tu banco de preguntas
            startActivity(intent)
        }
    }
}