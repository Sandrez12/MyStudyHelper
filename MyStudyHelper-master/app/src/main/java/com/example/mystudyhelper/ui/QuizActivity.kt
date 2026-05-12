package com.example.mystudyhelper.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R

// Estructura de nuestras preguntas
data class Pregunta(
    val texto: String,
    val opciones: List<String>,
    val indiceCorrecto: Int // 0, 1, 2 o 3 dependiendo de la respuesta correcta
)

class QuizActivity : AppCompatActivity() {

    private lateinit var tvPregunta: TextView
    private lateinit var progressBarQuiz: ProgressBar
    private lateinit var opcionesBtns: List<Button>

    private var preguntaActualIndex = 0
    private var puntaje = 0

    // ¡Nuestra base de datos de preguntas locales!
    private val listaPreguntas = listOf(
        Pregunta("Resuelve la ecuación: 3x + 5 = 17",
            listOf("x = 2", "x = 4", "x = 6", "x = 12"), 1),

        Pregunta("Si Marcus Fenix tiene 'x' granadas, usa 2 en combate y le quedan 4. ¿Cuál es la ecuación correcta?",
            listOf("x + 2 = 4", "2x = 4", "x - 2 = 4", "x / 2 = 4"), 2),

        Pregunta("Los Ravens anotaron 'y' touchdowns (de 7 puntos) y 2 goles de campo (3 puntos c/u), sumando 27 puntos. ¿Cuál es la ecuación?",
            listOf("7y + 6 = 27", "y + 6 = 27", "7y + 2 = 27", "7y - 6 = 27"), 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvPregunta = findViewById(R.id.tvPregunta)
        progressBarQuiz = findViewById(R.id.progressBarQuiz)

        opcionesBtns = listOf(
            findViewById(R.id.btnOpcion1),
            findViewById(R.id.btnOpcion2),
            findViewById(R.id.btnOpcion3),
            findViewById(R.id.btnOpcion4)
        )

        findViewById<ImageView>(R.id.btnCerrarQuiz).setOnClickListener { finish() }

        cargarPregunta()
    }

    private fun cargarPregunta() {
        // Actualizamos la barra de progreso
        val progreso = ((preguntaActualIndex.toFloat() / listaPreguntas.size) * 100).toInt()
        progressBarQuiz.progress = progreso

        if (preguntaActualIndex < listaPreguntas.size) {
            val preguntaActual = listaPreguntas[preguntaActualIndex]
            tvPregunta.text = preguntaActual.texto

            for (i in opcionesBtns.indices) {
                opcionesBtns[i].text = preguntaActual.opciones[i]
                // Restauramos el color blanco de los botones
                opcionesBtns[i].setBackgroundColor(Color.WHITE)
                opcionesBtns[i].setTextColor(Color.DKGRAY)

                // Asignamos la acción de comprobar respuesta
                opcionesBtns[i].setOnClickListener { comprobarRespuesta(i) }
            }
        } else {
            terminarQuiz()
        }
    }

    private fun comprobarRespuesta(indiceSeleccionado: Int) {
        val preguntaActual = listaPreguntas[preguntaActualIndex]

        // Desactivamos botones temporalmente para que no den doble clic
        opcionesBtns.forEach { it.isEnabled = false }

        if (indiceSeleccionado == preguntaActual.indiceCorrecto) {
            // ¡Correcto! Pintamos de verde
            opcionesBtns[indiceSeleccionado].setBackgroundColor(Color.parseColor("#4CAF50"))
            opcionesBtns[indiceSeleccionado].setTextColor(Color.WHITE)
            puntaje++
        } else {
            // Incorrecto. Pintamos de rojo la que eligió, y de verde la correcta
            opcionesBtns[indiceSeleccionado].setBackgroundColor(Color.parseColor("#F44336"))
            opcionesBtns[indiceSeleccionado].setTextColor(Color.WHITE)

            opcionesBtns[preguntaActual.indiceCorrecto].setBackgroundColor(Color.parseColor("#4CAF50"))
            opcionesBtns[preguntaActual.indiceCorrecto].setTextColor(Color.WHITE)
        }

        // Esperamos 1.5 segundos para que vea el resultado y pasamos a la siguiente
        Handler(Looper.getMainLooper()).postDelayed({
            preguntaActualIndex++
            opcionesBtns.forEach { it.isEnabled = true }
            cargarPregunta()
        }, 1500)
    }

    private fun terminarQuiz() {
        progressBarQuiz.progress = 100
        Toast.makeText(this, "¡Quiz terminado! Acertaste $puntaje de ${listaPreguntas.size}", Toast.LENGTH_LONG).show()
        // Aquí luego guardaremos el puntaje en la Base de Datos Room
        finish()
    }
}