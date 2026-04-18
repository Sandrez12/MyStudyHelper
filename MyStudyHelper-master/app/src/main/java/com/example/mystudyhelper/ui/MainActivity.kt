package com.example.mystudyhelper.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R
import com.example.mystudyhelper.viewmodel.MainViewModel
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    // Conectamos la Vista con el Cerebro (ViewModel)
    private val viewModel: MainViewModel by viewModels()

    // ==========================================
    // 1. LANZADOR DE PERMISOS IN-APP (NATIVO)
    // ==========================================
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

        if (cameraGranted && audioGranted) {
            Toast.makeText(this, "¡Excelente! Permisos concedidos.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Iniciando modo básico. Pediremos permisos cuando sea necesario.", Toast.LENGTH_LONG).show()
        }

        // Sin importar si aceptó o rechazó, lo dejamos pasar a estudiar (Permisos Diferidos)
        irAPantallaDeEstudio()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ==========================================
        // 2. BOTÓN DE REGISTRO
        // ==========================================
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        btnCrearCuenta.setOnClickListener {
            viewModel.onRegisterClicked()
        }

        // ==========================================
        // 3. SENSOR LÓGICO: INTERCEPTOR DE SALIDA
        // ==========================================
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Le avisamos al ViewModel que el usuario intentó salir (Gesto Atrás)
                viewModel.onExitAttempt()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // ==========================================
        // 4. OBSERVADOR: INTERVENCIÓN DE ESTRÉS
        // ==========================================
        viewModel.showIntervention.observe(this) { isStressed ->
            if (isStressed) {
                // Si hay estrés (3 intentos rápidos), lanzamos la pantalla de Break
                val intent = Intent(this, InterventionActivity::class.java)
                startActivity(intent)
            }
        }

        // ==========================================
        // 5. OBSERVADOR: POP-UP DE PERMISOS
        // ==========================================
        viewModel.showPermissionsDialog.observe(this) { show ->
            if (show) {
                showPermissionsPopup()
            }
        }
    }

    // ==========================================
    // 6. DISEÑO: DIÁLOGO DE PERMISOS (Boceto)
    // ==========================================
    private fun showPermissionsPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Antes de empezar! ⚠️")
        builder.setMessage("Necesitamos acceso a tu micrófono y cámara para que puedas tener una mejor experiencia en tus sesiones de estudio.\n\nNota: Solo los usaremos cuando tú nos lo permitas.")

        // Botón Principal - Lanza el cuadro nativo de Android
        builder.setPositiveButton("Continuar") { _, _ ->
            viewModel.onPermissionsConfirmed()
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }

        // Botón Secundario - Flujo Diferido
        builder.setNegativeButton("Ahora no") { dialog, _ ->
            viewModel.onPermissionsDismissed()
            dialog.dismiss()

            Toast.makeText(this, "Iniciando modo básico.", Toast.LENGTH_SHORT).show()
            irAPantallaDeEstudio()
        }

        val dialog = builder.create()
        dialog.show()
    }

    // ==========================================
    // 7. NAVEGACIÓN PRINCIPAL
    // ==========================================
    private fun irAPantallaDeEstudio() {
        // 1. Buscamos la cajita de texto donde el usuario escribió su nombre
        val etNombre = findViewById<android.widget.EditText>(R.id.etNombre)
        val nombreEscrito = etNombre.text.toString().trim()

        // Si por alguna razón lo dejó en blanco, le ponemos "Estudiante" por defecto
        val nombreFinal = if (nombreEscrito.isNotEmpty()) nombreEscrito else "Estudiante"

        // 2. Preparamos el viaje a la pantalla de Bienvenida
        val intent = Intent(this, WelcomeActivity::class.java)

        // 3. Metemos el nombre en la "mochila" del Intent usando una etiqueta ("NOMBRE_USUARIO")
        intent.putExtra("NOMBRE_USUARIO", nombreFinal)

        startActivity(intent)
        finish()
    }
}