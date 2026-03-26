package com.example.mystudyhelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // IMPORTANTE
import androidx.compose.runtime.getValue     // IMPORTANTE
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mystudyhelper.viewmodel.EjercicioViewModel

@Composable
fun EjercicioScreen(viewModel: EjercicioViewModel, onTriggerSaving: () -> Unit) {
    // Si la palabra 'attempts' o 'showAlert' sale en rojo, es por los imports de arriba
    val attempts by viewModel.exitAttempts.collectAsState()
    val showAlert by viewModel.showStressAlert.collectAsState()

    androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (showAlert) {
            androidx.compose.material3.Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.Yellow)) {
                androidx.compose.material3.Text("¿Estás estresado?", modifier = Modifier.padding(8.dp))
            }
        }
        // ... resto del código
        androidx.compose.material3.Button(onClick = { viewModel.onExitClicked(onTriggerSaving) }) {
            androidx.compose.material3.Text("Salir ($attempts)")
        }
    }
}