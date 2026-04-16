package com.example.mystudyhelper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R
import com.example.mystudyhelper.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INTERCEPTOR DEL SENSOR: Escucha gestos de "Atrás" del sistema
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onExitAttempt()
                // Opcional: Toast informativo para que tú veas que sí detecta el gesto
                Toast.makeText(this@MainActivity, "Gesto detectado", Toast.LENGTH_SHORT).show()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        // REACCIÓN DEL SENSOR: Observa cuando el ViewModel detecta estrés
        viewModel.showIntervention.observe(this) { isStressed ->
            if (isStressed) {
                Toast.makeText(
                    this,
                    "⚠️ Pareces estresado. ¿Quieres ver material o tomar un descanso?",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}