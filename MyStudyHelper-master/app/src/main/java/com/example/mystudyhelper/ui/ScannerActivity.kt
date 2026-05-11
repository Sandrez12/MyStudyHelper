package com.example.mystudyhelper.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.mystudyhelper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnCapturar: FloatingActionButton
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    // Diálogo de carga para el usuario
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
        btnCapturar = findViewById(R.id.btnCapturar)

        // Inicializamos el ejecutor para guardar fotos en segundo plano sin trabar la app
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        btnCapturar.setOnClickListener {
            tomaFotoFijaYAnaliza()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            // Optimizamos ImageCapture para máxima calidad de imagen
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // ==========================================================
    // NUEVO FLUJO MEJORADO: TOMA FOTO FIJA -> ANALIZA TEMP FILE
    // ==========================================================
    private fun tomaFotoFijaYAnaliza() {
        val imageCapture = imageCapture ?: return

        // 1. Mostramos UI de carga para que el usuario sepa que estamos trabajando
        showLoadingDialog()

        // 2. Creamos un archivo temporal para guardar la foto
        val photoFile = File(
            cacheDir, // Se guarda en caché, se borra automáticamente después
            "ocr_capture_${System.currentTimeMillis()}.jpg"
        )

        // Configuración de salida
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 3. Tomamos la foto fija real
        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor, // Lo hacemos en segundo plano
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // 4. La foto se guardó con éxito. Ahora la analizamos.
                    Handler(Looper.getMainLooper()).post {
                        procesarFotoFija(photoFile)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Handler(Looper.getMainLooper()).post {
                        dismissLoadingDialog()
                        Toast.makeText(baseContext, "Error al capturar foto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    private fun procesarFotoFija(photoFile: File) {
        // 5. Convertimos el archivo de imagen a InputImage de ML Kit
        val image = InputImage.fromFilePath(this, android.net.Uri.fromFile(photoFile))
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // 6. Iniciamos el reconocimiento de texto sobre la foto FIJA y NÍTIDA
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Limpiamos el texto (quitamos saltos de línea y espacios)
                val ecuacionLeida = visionText.text.replace("\n", "").replace(" ", "")

                // Ocultamos diálogo de carga
                dismissLoadingDialog()

                if (ecuacionLeida.isNotEmpty()) {
                    // 7. Resolvemos la ecuación matemática
                    resolverEcuacion(ecuacionLeida)
                } else {
                    Toast.makeText(this, "No detecté nada en la foto. Intenta enfocar mejor.", Toast.LENGTH_SHORT).show()
                }

                // Borramos el archivo temporal para no llenar el celular
                photoFile.delete()
            }
            .addOnFailureListener { e ->
                dismissLoadingDialog()
                Toast.makeText(this, "Error al leer texto de la foto", Toast.LENGTH_SHORT).show()
                photoFile.delete()
            }
    }

    private fun resolverEcuacion(ecuacion: String) {
        try {
            // Reemplazos básicos para ayudar a la librería (exp4j usa '*' para multiplicar)
            val ecuacionLimpia = ecuacion.replace("x", "*").replace("X", "*").replace("=","")

            val result = ExpressionBuilder(ecuacionLimpia).build().evaluate()
            mostrarResultado(ecuacion, result.toString())
        } catch (e: Exception) {
            // Si tiene 'x' algebraicas u otras cosas que exp4j no sabe calcular directamente
            mostrarResultado(ecuacion, "Es Álgebra (No aritmética simple). Falta conectar Rigoberta para resolver.")
        }
    }

    // ==========================================================
    // UI HELPERS (Diálogos de carga y resultados)
    // ==========================================================
    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.activity_subjects, null) // Usamos un layout cualquiera para el loading

        // Creamos un diálogo simple con un ProgressBar
        val progressBar = ProgressBar(this)
        progressBar.setPadding(50, 50, 50, 50)

        processingDialog = AlertDialog.Builder(this)
            .setTitle("🧠 Analizando foto...")
            .setMessage("Espera un momento mientras leo la ecuación")
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
            .setTitle("✨ ¡Ecuación Capturada!")
            .setMessage("Leímos:\n$ecuacion\n\nResultado:\n$resultado")
            .setPositiveButton("Volver a Escanear") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown() // Cerramos el hilo de fondo al cerrar la pantalla
    }
}