package com.example.mystudyhelper.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R
import com.example.mystudyhelper.viewmodel.MainViewModel

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

        // Sin importar si aceptó o rechazó, lo dejamos pasar a la Bienvenida
        irAPantallaDeEstudio()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ==========================================
        // 2. BOTÓN DE REGISTRO CON VALIDACIONES
        // ==========================================
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            // Obtenemos las cajas de texto
            val etNombre = findViewById<EditText>(R.id.etNombre)
            val etCorreo = findViewById<EditText>(R.id.etCorreo)
            val etPassword = findViewById<EditText>(R.id.etPassword)
            val etEdad = findViewById<EditText>(R.id.etEdad)

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()

            // Bandera de validación
            var esValido = true

            // Validar Nombre
            if (nombre.isEmpty()) {
                etNombre.error = "El nombre es obligatorio"
                esValido = false
            }

            // Validar Correo
            if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.error = "Ingresa un formato de correo válido"
                esValido = false
            }

            // Validar Contraseña (Mínimo 8, 1 mayúscula, 1 número)
            if (password.length < 8) {
                etPassword.error = "Debe tener al menos 8 caracteres"
                esValido = false
            } else if (!password.any { it.isUpperCase() }) {
                etPassword.error = "Debe contener al menos una letra mayúscula"
                esValido = false
            } else if (!password.any { it.isDigit() }) {
                etPassword.error = "Debe contener al menos un número"
                esValido = false
            }

            // Validar Edad
            if (edadStr.isEmpty()) {
                etEdad.error = "Ingresa tu edad"
                esValido = false
            } else {
                val edad = edadStr.toIntOrNull()
                if (edad == null || edad < 13 || edad > 99) {
                    etEdad.error = "Edad no válida (13-99)"
                    esValido = false
                }
            }

            // SI TODO ESTÁ PERFECTO, AVANZAMOS
            if (esValido) {
                // (Más adelante aquí guardaremos al usuario en Room)
                // Por ahora, disparamos el flujo del pop-up de permisos
                viewModel.onRegisterClicked()
            }
        }

        // ==========================================
        // 3. SENSOR LÓGICO: INTERCEPTOR DE SALIDA
        // ==========================================
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onExitAttempt()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // ==========================================
        // 4. OBSERVADORES DEL VIEWMODEL
        // ==========================================
        viewModel.showIntervention.observe(this) { isStressed ->
            if (isStressed) {
                val intent = Intent(this, InterventionActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.showPermissionsDialog.observe(this) { show ->
            if (show) {
                showPermissionsPopup()
            }
        }
    }

    // ==========================================
    // 5. DISEÑO: DIÁLOGO DE PERMISOS
    // ==========================================
    private fun showPermissionsPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Antes de empezar! ⚠️")
        builder.setMessage("Necesitamos acceso a tu micrófono y cámara para que puedas tener una mejor experiencia en tus sesiones de estudio.\n\nNota: Solo los usaremos cuando tú nos lo permitas.")

        builder.setPositiveButton("Continuar") { _, _ ->
            viewModel.onPermissionsConfirmed()
            requestPermissionsLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }

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
    // 6. NAVEGACIÓN A LA BIENVENIDA
    // ==========================================
    private fun irAPantallaDeEstudio() {
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val nombreEscrito = etNombre.text.toString().trim()
        val nombreFinal = if (nombreEscrito.isNotEmpty()) nombreEscrito else "Estudiante"

        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("NOMBRE_USUARIO", nombreFinal)
        startActivity(intent)

        // Destruimos la pantalla de registro para no volver con el botón "Atrás"
        finish()
    }
}