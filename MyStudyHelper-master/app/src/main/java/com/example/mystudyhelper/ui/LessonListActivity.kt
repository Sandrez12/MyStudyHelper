package com.example.mystudyhelper.ui
import com.example.mystudyhelper.model.Leccion
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.mystudyhelper.R
import com.example.mystudyhelper.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_list)

        findViewById<ImageView>(R.id.btnRegresarLista).setOnClickListener { finish() }

        // Recibimos qué materia quiere ver el usuario (ej. "Álgebra")
        val materia = intent.getStringExtra("MATERIA") ?: "Álgebra"
        findViewById<TextView>(R.id.tvTituloMateriaLista).text = "Temario: $materia"

        cargarListaDeTemas(materia)
    }

    private fun cargarListaDeTemas(materia: String) {
        val db = AppDatabase.getDatabase(this)
        val contenedor = findViewById<LinearLayout>(R.id.contenedorLecciones)

        lifecycleScope.launch(Dispatchers.IO) {
            val dao = db.leccionDao()

            // 1. POBLAR LA BASE DE DATOS (Si está vacía, metemos lecciones generales)
            if (dao.obtenerLeccionesPorMateria("Álgebra").isEmpty()) {
                val leccionesGenerales = listOf(
                    // ==========================================
                    // --- ARITMÉTICA ---
                    // ==========================================
                    Leccion(materia = "Aritmética", titulo = "Regla de Tres Simple",
                        contenidoTeorico = "Herramienta para resolver proporciones. Si conoces tres valores, puedes descubrir el cuarto multiplicando cruzado y dividiendo.",
                        ejemploPractico = "Si descargas un archivo de 80 GB y ya bajaron 10 GB en 15 minutos, calculas el total así:\n\n10 GB -> 15 min\n80 GB -> x min\n\nx = (80 * 15) / 10 = 120 minutos."),

                    Leccion(materia = "Aritmética", titulo = "Porcentajes y Descuentos",
                        contenidoTeorico = "Un porcentaje es una fracción de 100. Para aplicarlo a un precio, multiplicas el costo por el porcentaje (en formato decimal).",
                        ejemploPractico = "Ves una prenda de 1,200 pesos con 20% de descuento. El descuento es 1,200 * 0.20 = 240 pesos. Terminarás pagando solo 960 pesos."),

                    Leccion(materia = "Aritmética", titulo = "Fracciones Cotidianas",
                        contenidoTeorico = "Las fracciones representan partes de un entero. Son súper útiles cuando necesitas dividir cosas de forma equitativa o ajustar cantidades.",
                        ejemploPractico = "Si una receta para 4 personas te pide 1/2 taza de leche, y quieres hacerla para 8 personas (el doble), simplemente multiplicas 1/2 * 2 = 1 taza entera."),

                    Leccion(materia = "Aritmética", titulo = "Jerarquía de Operaciones",
                        contenidoTeorico = "También conocida como PEMDAS. Define el orden correcto para resolver problemas matemáticos: Paréntesis, Exponentes, Multiplicación/División y al final Suma/Resta.",
                        ejemploPractico = "¿Cuánto es 2 + 2 * 4?\nSi sumas primero, te da 16 (Incorrecto).\nSiguiendo la regla, primero multiplicas (2 * 4 = 8) y luego sumas 2. El resultado real es 10."),

                    // ==========================================
                    // --- ÁLGEBRA ---
                    // ==========================================
                    Leccion(materia = "Álgebra", titulo = "Ecuaciones de Primer Grado",
                        contenidoTeorico = "Buscan descubrir un valor desconocido (x). El truco está en pasar los números al otro lado del signo igual, siempre haciendo la operación contraria.",
                        ejemploPractico = "Compras 3 artículos iguales y pagas 30 de envío. El total fue de 480 pesos. ¿Cuánto costó cada artículo?\n\n3x + 30 = 480\n3x = 450\nx = 150 pesos."),

                    Leccion(materia = "Álgebra", titulo = "Sistemas de Ecuaciones",
                        contenidoTeorico = "Conjunto de dos o más ecuaciones con varias incógnitas. Buscamos valores que hagan reales todas las condiciones al mismo tiempo.",
                        ejemploPractico = "En el cine se vendieron 500 boletos. Adultos a 100 y niños a 50. Si se recaudaron 40,000 pesos, un sistema de ecuaciones te dice cuántos adultos y niños entraron."),

                    Leccion(materia = "Álgebra", titulo = "Lenguaje Algebraico",
                        contenidoTeorico = "Es el arte de traducir problemas leídos en palabras a expresiones matemáticas, usando letras para representar números que aún no conocemos.",
                        ejemploPractico = "Si lees la frase 'El doble de mi edad más 5 años', se traduce matemáticamente como: 2x + 5. Así puedes empezar a crear tus propias ecuaciones."),

                    Leccion(materia = "Álgebra", titulo = "Sucesiones y Patrones",
                        contenidoTeorico = "Son listas de números que siguen una regla lógica y constante (como sumar o multiplicar siempre por el mismo número).",
                        ejemploPractico = "Si ahorras 50 pesos la primera semana, 100 la segunda, y 150 la tercera, el patrón es sumar 50. La fórmula general sería 50n, donde 'n' es la semana."),

                    // ==========================================
                    // --- GEOMETRÍA ---
                    // ==========================================
                    Leccion(materia = "Geometría", titulo = "Teorema de Pitágoras",
                        contenidoTeorico = "En un triángulo rectángulo, el cuadrado de la hipotenusa (el lado más largo) es igual a la suma de los cuadrados de los otros dos lados.",
                        ejemploPractico = "Las televisiones se miden en diagonal. Si tu TV mide 40 de ancho y 30 de alto:\n40² + 30² = C²\n1600 + 900 = 2500\nLa raíz de 2500 te da exactamente 50 pulgadas."),

                    Leccion(materia = "Geometría", titulo = "Áreas y Perímetros",
                        contenidoTeorico = "El perímetro es el contorno de una figura, y el área es todo el espacio plano interior que ocupa.",
                        ejemploPractico = "Si vas a pintar tu cuarto, calculas el Área (Base x Altura) de cada pared para comprar pintura. Si vas a poner zoclo en el piso, calculas el Perímetro sumando los lados."),

                    Leccion(materia = "Geometría", titulo = "Cálculo de Volúmenes",
                        contenidoTeorico = "El volumen mide el espacio tridimensional que ocupa un objeto. Es fundamental para calcular capacidades de almacenamiento o líquidos.",
                        ejemploPractico = "Para saber cuántos litros de agua necesitas para llenar una alberca rectangular, multiplicas: Largo x Ancho x Profundidad. Si te da 10 metros cúbicos, ¡son 10,000 litros!"),

                    Leccion(materia = "Geometría", titulo = "Ángulos y Grados",
                        contenidoTeorico = "Los ángulos miden la apertura entre dos líneas que se cruzan. Se miden en grados, donde un círculo completo tiene 360 grados.",
                        ejemploPractico = "Al ajustar los espejos de un auto, buscas eliminar el 'punto ciego'. Un espejo mal posicionado crea un ángulo cerrado donde no puedes ver los autos que vienen a un lado.")
                )
                dao.insertarLecciones(leccionesGenerales)
            }

            // 2. BUSCAR LOS TEMAS DE LA MATERIA ELEGIDA
            val lecciones = dao.obtenerLeccionesPorMateria(materia)

            // 3. DIBUJAR LOS BOTONES EN PANTALLA
            withContext(Dispatchers.Main) {
                // Limpiamos el contenedor por si había botones viejos
                contenedor.removeAllViews()

                for (leccion in lecciones) {
                    val card = CardView(this@LessonListActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply { setMargins(0, 0, 0, 24) }
                        radius = 24f
                        cardElevation = 4f
                        setCardBackgroundColor(android.graphics.Color.WHITE)
                    }

                    val textoTitulo = TextView(this@LessonListActivity).apply {
                        text = "📖 ${leccion.titulo}"
                        textSize = 18f
                        setTextColor(android.graphics.Color.parseColor("#333333"))
                        setPadding(40, 40, 40, 40)
                    }

                    card.addView(textoTitulo)

                    // Al darle clic, viajamos a StudyActivity pasándole el ID exacto
                    card.setOnClickListener {
                        val intent = Intent(this@LessonListActivity, StudyActivity::class.java)
                        intent.putExtra("LECCION_ID", leccion.id)
                        startActivity(intent)
                    }

                    contenedor.addView(card)
                }
            }
        }
    }
}