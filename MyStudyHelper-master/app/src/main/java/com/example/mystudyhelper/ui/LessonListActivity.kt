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

            // 1. POBLAR LA BASE DE DATOS (Solo inserta la materia que el usuario tocó si está vacía)
            if (dao.obtenerLeccionesPorMateria(materia).isEmpty()) {

                val leccionesNuevas = when(materia) {
                    "Aritmética" -> listOf(
                        Leccion(materia = "Aritmética", titulo = "Regla de Tres Simple", contenidoTeorico = "Herramienta para resolver proporciones. Si conoces tres valores, puedes descubrir el cuarto multiplicando cruzado y dividiendo.", ejemploPractico = "Si descargas un archivo de 80 GB y ya bajaron 10 GB en 15 minutos, calculas el total así:\n\n10 GB -> 15 min\n80 GB -> x min\n\nx = (80 * 15) / 10 = 120 minutos."),
                        Leccion(materia = "Aritmética", titulo = "Porcentajes y Descuentos", contenidoTeorico = "Un porcentaje es una fracción de 100. Para aplicarlo a un precio, multiplicas el costo por el porcentaje (en formato decimal).", ejemploPractico = "Ves una prenda de 1,200 pesos con 20% de descuento. El descuento es 1,200 * 0.20 = 240 pesos. Terminarás pagando solo 960 pesos."),
                        Leccion(materia = "Aritmética", titulo = "Fracciones Cotidianas", contenidoTeorico = "Las fracciones representan partes de un entero. Son súper útiles cuando necesitas dividir cosas de forma equitativa o ajustar cantidades.", ejemploPractico = "Si una receta para 4 personas te pide 1/2 taza de leche, y quieres hacerla para 8 personas (el doble), simplemente multiplicas 1/2 * 2 = 1 taza entera."),
                        Leccion(materia = "Aritmética", titulo = "Jerarquía de Operaciones", contenidoTeorico = "También conocida como PEMDAS. Define el orden correcto para resolver problemas matemáticos: Paréntesis, Exponentes, Multiplicación/División y al final Suma/Resta.", ejemploPractico = "¿Cuánto es 2 + 2 * 4?\nSi sumas primero, te da 16 (Incorrecto).\nSiguiendo la regla, primero multiplicas (2 * 4 = 8) y luego sumas 2. El resultado real es 10.")
                    )
                    "Álgebra" -> listOf(
                        Leccion(materia = "Álgebra", titulo = "Ecuaciones de Primer Grado", contenidoTeorico = "Buscan descubrir un valor desconocido (x). El truco está en pasar los números al otro lado del signo igual, siempre haciendo la operación contraria.", ejemploPractico = "Compras 3 artículos iguales y pagas 30 de envío. El total fue de 480 pesos. ¿Cuánto costó cada artículo?\n\n3x + 30 = 480\n3x = 450\nx = 150 pesos."),
                        Leccion(materia = "Álgebra", titulo = "Sistemas de Ecuaciones", contenidoTeorico = "Conjunto de dos o más ecuaciones con varias incógnitas. Buscamos valores que hagan reales todas las condiciones al mismo tiempo.", ejemploPractico = "En el cine se vendieron 500 boletos. Adultos a 100 y niños a 50. Si se recaudaron 40,000 pesos, un sistema de ecuaciones te dice cuántos adultos y niños entraron."),
                        Leccion(materia = "Álgebra", titulo = "Lenguaje Algebraico", contenidoTeorico = "Es el arte de traducir problemas leídos en palabras a expresiones matemáticas, usando letras para representar números que aún no conocemos.", ejemploPractico = "Si lees la frase 'El doble de mi edad más 5 años', se traduce matemáticamente como: 2x + 5. Así puedes empezar a crear tus propias ecuaciones."),
                        Leccion(materia = "Álgebra", titulo = "Sucesiones y Patrones", contenidoTeorico = "Son listas de números que siguen una regla lógica y constante (como sumar o multiplicar siempre por el mismo número).", ejemploPractico = "Si ahorras 50 pesos la primera semana, 100 la segunda, y 150 la tercera, el patrón es sumar 50. La fórmula general sería 50n, donde 'n' es la semana.")
                    )
                    "Geometría" -> listOf(
                        Leccion(materia = "Geometría", titulo = "Teorema de Pitágoras", contenidoTeorico = "En un triángulo rectángulo, el cuadrado de la hipotenusa (el lado más largo) es igual a la suma de los cuadrados de los otros dos lados.", ejemploPractico = "Las televisiones se miden en diagonal. Si tu TV mide 40 de ancho y 30 de alto:\n40² + 30² = C²\n1600 + 900 = 2500\nLa raíz de 2500 te da exactamente 50 pulgadas."),
                        Leccion(materia = "Geometría", titulo = "Áreas y Perímetros", contenidoTeorico = "El perímetro es el contorno de una figura, y el área es todo el espacio plano interior que ocupa.", ejemploPractico = "Si vas a pintar tu cuarto, calculas el Área (Base x Altura) de cada pared para comprar pintura. Si vas a poner zoclo en el piso, calculas el Perímetro sumando los lados."),
                        Leccion(materia = "Geometría", titulo = "Cálculo de Volúmenes", contenidoTeorico = "El volumen mide el espacio tridimensional que ocupa un objeto. Es fundamental para calcular capacidades de almacenamiento o líquidos.", ejemploPractico = "Para saber cuántos litros de agua necesitas para llenar una alberca rectangular, multiplicas: Largo x Ancho x Profundidad. Si te da 10 metros cúbicos, ¡son 10,000 litros!"),
                        Leccion(materia = "Geometría", titulo = "Ángulos y Grados", contenidoTeorico = "Los ángulos miden la apertura entre dos líneas que se cruzan. Se miden en grados, donde un círculo completo tiene 360 grados.", ejemploPractico = "Al ajustar los espejos de un auto, buscas eliminar el 'punto ciego'. Un espejo mal posicionado crea un ángulo cerrado donde no puedes ver los autos que vienen a un lado.")
                    )
                    "Programación" -> listOf(
                        Leccion(materia = "Programación", titulo = "Programación Orientada a Objetos", contenidoTeorico = "Paradigma basado en 'clases' (moldes) y 'objetos' (creaciones) que agrupan datos y comportamientos para reutilizar código.", ejemploPractico = "Al crear una app móvil, puedes tener una clase 'Usuario'. Cada vez que alguien se registra, creas un nuevo objeto Usuario que guarda su correo y contraseña."),
                        Leccion(materia = "Programación", titulo = "Arquitectura Cliente-Servidor", contenidoTeorico = "Modelo donde un 'cliente' solicita recursos o servicios a un 'servidor' remoto que procesa y devuelve la información.", ejemploPractico = "Al iniciar sesión, tu celular (el cliente) hace una petición a la base de datos en la nube (el servidor), la cual responde si el usuario es válido para dejarte entrar.")
                    )
                    "Graficación" -> listOf(
                        Leccion(materia = "Graficación", titulo = "Rasterización vs Vectoriales", contenidoTeorico = "Las imágenes rasterizadas están hechas de píxeles y pierden calidad al hacer zoom. Las vectoriales son ecuaciones matemáticas, permitiendo zoom infinito.", ejemploPractico = "Para diseñar los íconos de la interfaz de una aplicación, siempre se usan formatos vectoriales (como SVG) para que se vean súper nítidos."),
                        Leccion(materia = "Graficación", titulo = "Transformaciones 3D", contenidoTeorico = "Para manipular un objeto en el espacio tridimensional, se multiplican sus vértices por una matriz de transformación matemática.", ejemploPractico = "En un videojuego, cuando haces que tu personaje corra y apunte, el motor gráfico calcula constantemente matrices de rotación.")
                    )
                    "Sistemas Embebidos" -> listOf(
                        Leccion(materia = "Sistemas Embebidos", titulo = "Microcontroladores e IoT", contenidoTeorico = "Un microcontrolador es una computadora compacta en un solo chip. El IoT los conecta a la red.", ejemploPractico = "Puedes usar una placa como el ESP32 para controlar una maceta inteligente y mandar el estado de la planta a tu celular."),
                        Leccion(materia = "Sistemas Embebidos", titulo = "Sensores y Actuadores", contenidoTeorico = "Los sensores recopilan datos del entorno (entradas). Los actuadores realizan una acción mecánica (salidas).", ejemploPractico = "Si el sensor de humedad de la tierra detecta que está seca, el microcontrolador enciende una bomba de agua (actuador) para regarla.")
                    )
                    "A1" -> listOf(
                        Leccion(materia = "A1", titulo = "Basic Greetings & To Be", contenidoTeorico = "The verb 'To Be' (am, is, are) is used to describe identity, states, and locations.", ejemploPractico = "To introduce yourself in a professional meeting: 'Hello, I am Héctor and I am a software engineering student.'")
                    )
                    "A2" -> listOf(
                        Leccion(materia = "A2", titulo = "Past Simple: Regular Verbs", contenidoTeorico = "Used to talk about completed actions in the past. Regular verbs add '-ed' at the end.", ejemploPractico = "If you finished a coding task yesterday: 'I updated the app repository and fixed three bugs.'")
                    )
                    "B1" -> listOf(
                        Leccion(materia = "B1", titulo = "Present Perfect: Experiences", contenidoTeorico = "Used for actions that happened at an unspecified time in the past. Uses 'Have/Has' + Past Participle.", ejemploPractico = "To talk about your tech experience: 'I have worked with Kotlin for three months now.'")
                    )
                    "B2" -> listOf(
                        Leccion(materia = "B2", titulo = "The First Conditional", contenidoTeorico = "Used to talk about real possibilities in the future. Structure: If + Present Simple, Will + Verb.", ejemploPractico = "In project planning: 'If we optimize the database, the app will run faster.'")
                    )
                    "C1" -> listOf(
                        Leccion(materia = "C1", titulo = "Advanced Connectors", contenidoTeorico = "Used to create complex and formal arguments.", ejemploPractico = "In a formal report: 'The testing phase was successful; nevertheless, additional debugging is paramount.'")
                    )
                    else -> emptyList() // Si la materia no existe, devuelve una lista vacía
                }

                // Guardamos en la base de datos solo si encontramos la materia en la lista de arriba
                if (leccionesNuevas.isNotEmpty()) {
                    dao.insertarLecciones(leccionesNuevas)
                }
            }

            // 2. BUSCAR LOS TEMAS DE LA MATERIA ELEGIDA
            val lecciones = dao.obtenerLeccionesPorMateria(materia)

            // 3. DIBUJAR LOS BOTONES EN PANTALLA
            withContext(Dispatchers.Main) {
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