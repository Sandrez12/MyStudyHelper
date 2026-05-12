package com.example.mystudyhelper.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.mystudyhelper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.objecthunter.exp4j.ExpressionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
class ScannerActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private var cameraProvider: ProcessCameraProvider? = null // Guardamos el control de la cámara
    private var processingDialog: AlertDialog? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) startCamera() else finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        viewFinder = findViewById(R.id.viewFinder)
        val btnCapturar = findViewById<FloatingActionButton>(R.id.btnCapturar)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        btnCapturar.setOnClickListener {
            congelarYAnalizar()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                Toast.makeText(this, "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // ==========================================================
    // NUEVA LÓGICA: CONGELA LA PANTALLA Y LEE EL BITMAP DIRECTO
    // ==========================================================
    // ==========================================================
    // NUEVA LÓGICA: CONGELA LA PANTALLA, RECORTA Y ANALIZA
    // ==========================================================
    private fun congelarYAnalizar() {
        val bitmapFijo: Bitmap? = viewFinder.bitmap

        if (bitmapFijo == null) {
            Toast.makeText(this, "Aún no carga la cámara", Toast.LENGTH_SHORT).show()
            return
        }

        cameraProvider?.unbindAll()
        showLoadingDialog()

        // ---------------------------------------------------
        // MAGIA NUEVA: Recortar el centro de la imagen
        // ---------------------------------------------------
        val width = bitmapFijo.width
        val height = bitmapFijo.height

        // Calculamos un área similar a tu marco morado (70% de ancho, 25% de alto)
        val anchoRecorte = (width * 0.7).toInt()
        val altoRecorte = (height * 0.25).toInt()

        // Encontramos las coordenadas del centro exacto
        val ejeX = (width - anchoRecorte) / 2
        val ejeY = (height - altoRecorte) / 2

        // Creamos una nueva imagen que solo contiene el pedacito del centro
        val bitmapRecortado = Bitmap.createBitmap(bitmapFijo, ejeX, ejeY, anchoRecorte, altoRecorte)
        // ---------------------------------------------------

        // Pasamos SOLO el recorte limpio a la Inteligencia Artificial
        val image = InputImage.fromBitmap(bitmapRecortado, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val ecuacionLeida = visionText.text.replace("\n", "").replace(" ", "")

                if (ecuacionLeida.isNotEmpty()) {
                    resolverEcuacion(ecuacionLeida)
                } else {
                    dismissLoadingDialog()
                    mostrarErrorYReiniciar("El recuadro está vacío o no entiendo la letra. Intenta centrarlo bien.")
                }
            }
            .addOnFailureListener { e ->
                dismissLoadingDialog()
                mostrarErrorYReiniciar("Error al leer la imagen.")
            }
    }

    // ==========================================================
    // EL NUEVO CEREBRO: CONEXIÓN A LA API DE NEWTON
    // ==========================================================
    private fun resolverEcuacion(ecuacion: String) {
        // Abrimos un hilo en segundo plano (Corrutina) para que la pantalla no se congele
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Preparamos el texto para que sea una URL válida (codificamos signos como '+')
                val ecuacionLimpia = ecuacion.replace(" ", "")
                val urlEncoded = URLEncoder.encode(ecuacionLimpia, "UTF-8")

                // 2. Hacemos la petición HTTP al servidor de Newton (endpoint de simplificación)
                val url = URL("https://newton.vercel.app/api/v2/simplify/$urlEncoded")
                val jsonResponse = url.readText() // Descargamos la respuesta

                // 3. La respuesta viene en formato JSON, extraemos solo el resultado
                val jsonObject = JSONObject(jsonResponse)
                val resultadoAPI = jsonObject.getString("result")

                // 4. Volvemos al hilo principal (Main) para actualizar la interfaz gráfica
                withContext(Dispatchers.Main) {
                    dismissLoadingDialog()
                    mostrarResultado(ecuacionLimpia, resultadoAPI)
                }

            } catch (e: Exception) {
                // Si no hay internet o la API no reconoce los garabatos matemáticos
                withContext(Dispatchers.Main) {
                    dismissLoadingDialog()
                    mostrarErrorYReiniciar("Leí '$ecuacion' pero no pude resolverla. Asegúrate de que sea una expresión clara.")
                }
            }
        }
    }

    // ==========================================================
    // UI HELPERS
    // ==========================================================
    private fun showLoadingDialog() {
        val progressBar = ProgressBar(this)
        progressBar.setPadding(50, 50, 50, 50)

        processingDialog = AlertDialog.Builder(this)
            .setTitle("🧠 Analizando...")
            .setMessage("Extrayendo números de la imagen")
            .setView(progressBar)
            .setCancelable(false)
            .create()
        processingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        processingDialog?.dismiss()
    }

    private fun mostrarResultado(ecuacion: String, resultado: String) {
        AlertDialog.Builder(this)
            .setTitle("✨ ¡Resuelto!")
            .setMessage("Detectamos:\n$ecuacion\n\nResultado:\n$resultado")
            .setPositiveButton("Escanear otra") { dialog, _ ->
                dialog.dismiss()
                startCamera() // Vuelve a encender el video en vivo
            }
            .setCancelable(false)
            .show()
    }

    private fun mostrarErrorYReiniciar(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Ups...")
            .setMessage(mensaje)
            .setPositiveButton("Reintentar") { dialog, _ ->
                dialog.dismiss()
                startCamera() // Vuelve a encender el video en vivo
            }
            .setCancelable(false)
            .show()
    }
}