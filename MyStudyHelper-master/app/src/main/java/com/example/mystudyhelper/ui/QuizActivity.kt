package com.example.mystudyhelper.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R
import com.google.android.material.button.MaterialButton

data class PreguntaQuiz(
    val textoPregunta: String,
    val opciones: List<String>,
    val indiceCorrecto: Int
)

class QuizActivity : AppCompatActivity() {

    private lateinit var tvPregunta: TextView
    private lateinit var btnOpcion1: MaterialButton
    private lateinit var btnOpcion2: MaterialButton
    private lateinit var btnOpcion3: MaterialButton
    private lateinit var btnOpcion4: MaterialButton
    private lateinit var btnComprobar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomZone: LinearLayout

    private var botonesOpciones = listOf<MaterialButton>()
    private var opcionSeleccionada = -1
    private var preguntaActualIndex = 0
    private var yaComprobado = false

    // Cambiamos a var para que la lista sea dinámica
    private var listaPreguntas = listOf<PreguntaQuiz>()
    private var materiaActual = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // 1. Recuperar la materia del Intent
        materiaActual = intent.getStringExtra("MATERIA") ?: "General"
        configurarBancoDePreguntas()

        // 2. Vincular vistas
        vincularVistas()

        findViewById<ImageView>(R.id.btnCerrarQuiz).setOnClickListener { finish() }

        // 3. Configurar clics
        botonesOpciones.forEachIndexed { index, boton ->
            boton.setOnClickListener {
                if (!yaComprobado) seleccionarOpcion(index)
            }
        }

        btnComprobar.setOnClickListener {
            if (!yaComprobado) {
                comprobarRespuesta()
            } else {
                avanzarSiguientePregunta()
            }
        }

        cargarPregunta()
    }

    private fun vincularVistas() {
        tvPregunta = findViewById(R.id.tvPreguntaQuiz)
        btnOpcion1 = findViewById(R.id.btnOpcion1)
        btnOpcion2 = findViewById(R.id.btnOpcion2)
        btnOpcion3 = findViewById(R.id.btnOpcion3)
        btnOpcion4 = findViewById(R.id.btnOpcion4)
        btnComprobar = findViewById(R.id.btnComprobar)
        progressBar = findViewById(R.id.progressBarQuiz)
        bottomZone = findViewById(R.id.bottomZone)
        botonesOpciones = listOf(btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4)
    }

    private fun configurarBancoDePreguntas() {
        listaPreguntas = when (materiaActual) {
            "Programación" -> listOf(
                PreguntaQuiz("En Programación Orientada a Objetos, ¿qué concepto permite ocultar los detalles internos de una clase?", listOf("Herencia", "Polimorfismo", "Encapsulamiento", "Abstracción"), 2),
                PreguntaQuiz("¿Qué significa el acrónimo SQL en bases de datos?", listOf("Structured Query Language", "System Query Logic", "Standard Question Language", "Simple Query List"), 0),
                PreguntaQuiz("¿Cuál es la función principal de una API REST?", listOf("Diseñar interfaces gráficas", "Permitir la comunicación entre sistemas a través de HTTP", "Compilar código a lenguaje máquina", "Gestionar la memoria RAM"), 1)
            )
            "Sistemas Embebidos" -> listOf(
                PreguntaQuiz("¿Qué componente es considerado el 'cerebro' principal de un sistema embebido?", listOf("El actuador", "La fuente de poder", "El microcontrolador", "El sensor ultrasónico"), 2),
                PreguntaQuiz("¿Cuál de los siguientes componentes es un ejemplo de un 'actuador'?", listOf("Fotoresistencia (LDR)", "Motor Servomotor", "Sensor de temperatura (DHT11)", "Acelerómetro"), 1)
            )
            "B1", "A2", "A1", "English" -> listOf(
                PreguntaQuiz("Choose the correct past tense: 'Yesterday, she ___ to the library to study.'", listOf("goes", "gone", "went", "going"), 2),
                PreguntaQuiz("What is the correct translation for 'The database is currently offline'?", listOf("La base de datos es muy lenta", "La base de datos está fuera de línea actualmente", "El servidor de datos está lleno", "La conexión a la red se perdió"), 1),
                PreguntaQuiz("Complete the sentence (First Conditional): 'If it rains tomorrow, we ___ at home.'", listOf("would stay", "stayed", "will stay", "are staying"), 2)
            )
            "Álgebra" -> listOf(
                PreguntaQuiz("Si resuelves la ecuación 5x - 10 = 20, ¿cuál es el valor de x?", listOf("5", "6", "10", "4"), 1),
                PreguntaQuiz("¿Cómo se le llama a una expresión algebraica que consta de exactamente dos términos?", listOf("Monomio", "Trinomio", "Polinomio", "Binomio"), 3)
            )
            else -> listOf(
                PreguntaQuiz("Pregunta general: ¿Cuál es el resultado de 15 % 4 (Módulo)?", listOf("3", "4", "0", "1"), 0),
                PreguntaQuiz("¿Qué estructura de datos usa la lógica LIFO (Last In, First Out)?", listOf("Cola (Queue)", "Lista enlazada", "Pila (Stack)", "Árbol binario"), 2)
            )
        }
    }

    private fun cargarPregunta() {
        if (listaPreguntas.isEmpty()) return

        val preguntaActual = listaPreguntas[preguntaActualIndex]
        tvPregunta.text = preguntaActual.textoPregunta

        botonesOpciones.forEachIndexed { index, boton ->
            boton.text = preguntaActual.opciones[index]
            boton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            boton.setTextColor(Color.parseColor("#4B4B4B"))
            boton.strokeColor = ColorStateList.valueOf(Color.parseColor("#E0E0E0"))
        }

        opcionSeleccionada = -1
        yaComprobado = false
        btnComprobar.text = "COMPROBAR"
        btnComprobar.isEnabled = false
        btnComprobar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E0E0E0"))
        btnComprobar.setTextColor(Color.parseColor("#9E9E9E"))
        bottomZone.setBackgroundColor(Color.parseColor("#FFFFFF"))

        progressBar.progress = ((preguntaActualIndex + 1) * 100) / listaPreguntas.size
    }

    private fun seleccionarOpcion(index: Int) {
        opcionSeleccionada = index
        botonesOpciones.forEachIndexed { i, boton ->
            if (i == index) {
                boton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E1F5FE"))
                boton.strokeColor = ColorStateList.valueOf(Color.parseColor("#03A9F4"))
                boton.setTextColor(Color.parseColor("#0288D1"))
            } else {
                boton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                boton.strokeColor = ColorStateList.valueOf(Color.parseColor("#E0E0E0"))
                boton.setTextColor(Color.parseColor("#4B4B4B"))
            }
        }

        btnComprobar.isEnabled = true
        btnComprobar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#58CC02"))
        btnComprobar.setTextColor(Color.parseColor("#FFFFFF"))
    }

    private fun comprobarRespuesta() {
        yaComprobado = true
        val preguntaActual = listaPreguntas[preguntaActualIndex]

        if (opcionSeleccionada == preguntaActual.indiceCorrecto) {
            bottomZone.setBackgroundColor(Color.parseColor("#D7FFB8"))
            btnComprobar.text = "CONTINUAR"
            btnComprobar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#58CC02"))
        } else {
            bottomZone.setBackgroundColor(Color.parseColor("#FFDFE0"))
            btnComprobar.text = "ENTENDIDO"
            btnComprobar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF4B4B"))
            botonesOpciones[preguntaActual.indiceCorrecto].strokeColor = ColorStateList.valueOf(Color.parseColor("#58CC02"))
            botonesOpciones[preguntaActual.indiceCorrecto].backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D7FFB8"))
        }
    }

    private fun avanzarSiguientePregunta() {
        preguntaActualIndex++
        if (preguntaActualIndex < listaPreguntas.size) {
            cargarPregunta()
        } else {
            tvPregunta.text = "¡Lección completada! 🎉"
            botonesOpciones.forEach { it.visibility = View.GONE }
            btnComprobar.text = "VOLVER AL MENÚ"
            btnComprobar.setOnClickListener { finish() }
            bottomZone.setBackgroundColor(Color.parseColor("#FFFFFF"))
            btnComprobar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#03A9F4"))
        }
    }
}