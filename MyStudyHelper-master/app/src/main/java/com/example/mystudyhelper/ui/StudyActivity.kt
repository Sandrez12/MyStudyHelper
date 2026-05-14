package com.example.mystudyhelper.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystudyhelper.R
import com.example.mystudyhelper.model.AppDatabase
import com.example.mystudyhelper.model.Leccion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyActivity : AppCompatActivity() {

    private lateinit var tvMateriaHeader: TextView
    private lateinit var tvTituloLeccion: TextView
    private lateinit var tvContenidoTeorico: TextView
    private lateinit var tvEjemploPractico: TextView
    private lateinit var tvReloj: TextView

    // Declaramos el timer como opcional para evitar errores si se cierra la app rápido
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        // 1. Vinculamos las vistas
        tvMateriaHeader = findViewById(R.id.tvMateriaHeader)
        tvTituloLeccion = findViewById(R.id.tvTituloLeccion)
        tvContenidoTeorico = findViewById(R.id.tvContenidoTeorico)
        tvEjemploPractico = findViewById(R.id.tvEjemploPractico)
        tvReloj = findViewById(R.id.tvCronometroPomodoro)

        // Botón de regresar
        findViewById<ImageView>(R.id.btnRegresar).setOnClickListener {
            finish()
        }

        // Botón de terminar lección
        findViewById<Button>(R.id.btnTerminarLeccion).setOnClickListener {
            Toast.makeText(this, "¡Lección completada! Estás listo para practicar.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 2. Iniciamos las funciones principales
        cargarLeccionDesdeBD()
        iniciarTemporizadorRelajante()
    }

    private fun cargarLeccionDesdeBD() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.leccionDao()
        val idLeccionSeleccionada = intent.getIntExtra("LECCION_ID", -1)

        lifecycleScope.launch(Dispatchers.IO) {
            // Inyectamos datos si no existen
            val comprobacion = dao.obtenerLeccionesPorMateria("Álgebra")
            if (comprobacion.isEmpty()) {
                val leccionesGenerales = listOf(
                    Leccion(
                        materia = "Álgebra",
                        titulo = "Ecuaciones de Primer Grado",
                        contenidoTeorico = "Una ecuación es una igualdad matemática con incógnitas. El objetivo es 'despejar' la x.",
                        ejemploPractico = "3x + 30 = 480 -> x = 150."
                    ),
                    Leccion(
                        materia = "Inglés",
                        titulo = "Basic Greetings & To Be",
                        contenidoTeorico = "El verbo 'To Be' es ser o estar. (I am, You are, He is).",
                        ejemploPractico = "I am a student at BUAP."
                    )
                )
                dao.insertarLecciones(leccionesGenerales)
            }

            val leccionActual = if (idLeccionSeleccionada != -1) {
                dao.obtenerLeccionPorId(idLeccionSeleccionada)
            } else {
                dao.obtenerLeccionesPorMateria("Álgebra").firstOrNull()
            }

            withContext(Dispatchers.Main) {
                if (leccionActual != null) {
                    tvMateriaHeader.text = "Lección: ${leccionActual.materia}"
                    tvTituloLeccion.text = "🧠 ${leccionActual.titulo}"
                    tvContenidoTeorico.text = leccionActual.contenidoTeorico
                    tvEjemploPractico.text = leccionActual.ejemploPractico
                }
            }
        }
    }

    private fun iniciarTemporizadorRelajante() {
        // 25 minutos (1500000 ms)
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutos = (millisUntilFinished / 1000) / 60
                val segundos = (millisUntilFinished / 1000) % 60
                tvReloj.text = String.format("%02d:%02d", minutos, segundos)
            }

            override fun onFinish() {
                mostrarAlertaDescanso()
            }
        }.start()
    }

    private fun mostrarAlertaDescanso() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Tiempo de un respiro! 🌿")
        builder.setMessage("Has estudiado mucho. Tu cerebro necesita procesar la información.\n\nDesconéctate 5 minutos.")
        builder.setPositiveButton("Entendido") { dialog, _ ->
            dialog.dismiss()
            iniciarTemporizadorRelajante()
        }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelamos el timer solo si existe para evitar NullPointerException
        timer?.cancel()
    }
}