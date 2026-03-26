package com.example.mystudyhelper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystudyhelper.viewmodel.EjercicioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicamos el tema de Material 3
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyStudyHelperApp()
                }
            }
        }
    }
}

@Composable
fun MyStudyHelperApp() {
    val navController = rememberNavController()
    // Obtenemos el ViewModel compartido para todas las pantallas
    val exerciseViewModel: EjercicioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ) {
        // Pantalla 1: Registro de Luis
        composable("registro") {
            RegistroScreen(onNavigateNext = {
                navController.navigate("ejercicio")
            })
        }

        // Pantalla 2: Pregunta de Inglés y detección de estrés
        composable("ejercicio") {
            EjercicioScreen(
                viewModel = exerciseViewModel,
                onTriggerSaving = {
                    navController.navigate("carga_guardado")
                }
            )
        }

        // Pantalla 3: Simulación de guardado (2 segundos)
        composable("carga_guardado") {
            CargaScreen(onFinished = {
                navController.navigate("resolucion")
            })
        }

        // Pantalla 4: Intervención (Tarjetas Moradas)
        composable("resolucion") {
            ResolucionScreen(
                viewModel = exerciseViewModel,
                onBackToStart = {
                    // Limpiamos los datos antes de volver
                    exerciseViewModel.resetAll()
                    navController.navigate("ejercicio") {
                        // Evita que el usuario regrese a la pantalla de resolución con el botón atrás
                        popUpTo("registro") { inclusive = false }
                    }
                }
            )
        }
    }
}