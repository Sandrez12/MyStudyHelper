package com.example.mystudyhelper.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CargaScreen(onFinished: () -> Unit) {
    // Lógica MVP: Espera 2 segundos y navega automáticamente
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }

    // Contenedor principal centrado
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicador de carga circular (Material 3)
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            // Texto descriptivo del estado
            Text(text = "Guardando evento de estrés...")
        }
    }
}