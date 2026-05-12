package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        // 1. Vinculamos las vistas del XML con Kotlin
        tvMateriaHeader = findViewById(R.id.tvMateriaHeader)
        tvTituloLeccion = findViewById(R.id.tvTituloLeccion)
        tvContenidoTeorico = findViewById(R.id.tvContenidoTeorico)
        tvEjemploPractico = findViewById(R.id.tvEjemploPractico)

        // Botón de regresar
        findViewById<ImageView>(R.id.btnRegresar).setOnClickListener {
            finish()
        }

        // Botón de terminar lección
        findViewById<Button>(R.id.btnTerminarLeccion).setOnClickListener {
            Toast.makeText(this, "¡Lección completada! Estás listo para practicar.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 2. Cargamos los datos desde Room
        cargarLeccionDesdeBD()
    }

    private fun cargarLeccionDesdeBD() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.leccionDao()

        // Recibimos el ID que nos mandó la pantalla de la lista (-1 si hay error)
        val idLeccionSeleccionada = intent.getIntExtra("LECCION_ID", -1)

        lifecycleScope.launch(Dispatchers.IO) {

            // 1. INYECTAR DATOS GENERALES (Si la BD está vacía)
            var comprobacion = dao.obtenerLeccionesPorMateria("Álgebra")
            if (comprobacion.isEmpty()) {
                val leccionesGenerales = listOf(
                    Leccion(
                        materia = "Álgebra",
                        titulo = "Ecuaciones de Primer Grado",
                        contenidoTeorico = "Una ecuación es una igualdad matemática con incógnitas. El objetivo es 'despejar' la letra (como la x) para descubrir su valor, pasando los números al otro lado con la operación contraria.",
                        ejemploPractico = "Imagina que pides comida rápida con tus amigos. Compran 3 pizzas iguales y pagan un envío de 30 pesos. Si el total fue de 480 pesos, ¿cuánto costó cada pizza?\n\n3x + 30 = 480\n3x = 450\nx = 150 pesos."
                    ),
                    Leccion(
                        materia = "Álgebra",
                        titulo = "Sistemas de Ecuaciones",
                        contenidoTeorico = "Es un conjunto de dos o más ecuaciones con varias incógnitas. Buscamos los valores que hacen que todas las ecuaciones sean verdaderas al mismo tiempo.",
                        ejemploPractico = "En la taquilla de un partido de americano, se vendieron 500 boletos. Los de adulto costaban 100 y los de niño 50. Si se recaudaron 40,000 pesos, un sistema de ecuaciones te diría exactamente cuántos adultos y niños entraron al estadio."
                    ),
                    Leccion(
                        materia = "Aritmética",
                        titulo = "Regla de Tres Simple",
                        contenidoTeorico = "Es una herramienta para resolver problemas de proporciones. Si conoces tres valores, puedes descubrir el cuarto multiplicando cruzado y dividiendo.",
                        ejemploPractico = "Si estás descargando un videojuego de 80 GB y tu consola marca que descargó 10 GB en 15 minutos, puedes calcular cuánto tardará en total:\n\n10 GB -> 15 min\n80 GB -> x min\n\nx = (80 * 15) / 10 = 120 minutos."
                    )
                )
                dao.insertarLecciones(leccionesGenerales)
            }

            // 2. BUSCAR LA LECCIÓN ESPECÍFICA POR SU ID
            val leccionActual = if (idLeccionSeleccionada != -1) {
                dao.obtenerLeccionPorId(idLeccionSeleccionada)
            } else {
                // Si por alguna razón no llegó el ID, mostramos la primera de Álgebra como respaldo
                dao.obtenerLeccionesPorMateria("Álgebra").firstOrNull()
            }

            // 3. ACTUALIZAR LA PANTALLA
            withContext(Dispatchers.Main) {
                if (leccionActual != null) {
                    tvMateriaHeader.text = "Lección: ${leccionActual.materia}"
                    tvTituloLeccion.text = "🧠 ${leccionActual.titulo}"
                    tvContenidoTeorico.text = leccionActual.contenidoTeorico
                    tvEjemploPractico.text = leccionActual.ejemploPractico
                } else {
                    Toast.makeText(this@StudyActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}