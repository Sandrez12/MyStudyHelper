package com.example.mystudyhelper.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mystudyhelper.viewmodel.EjercicioViewModel

@Composable
fun ResolucionScreen(viewModel: EjercicioViewModel, onBackToStart: () -> Unit) {
    // Observamos qué opción morada está seleccionada
    val selectedOption by viewModel.selectedOption.collectAsState()
    val opciones = listOf("Ver Material de Apoyo", "Tomar un Descanso de 5 min", "Pedir Ayuda")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Plan de Intervención",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona una acción para reducir tu estrés:",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Generamos las tarjetas dinámicamente
        opciones.forEachIndexed { index, texto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { viewModel.selectIntervention(index) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == index) Color(0xFFBB86FC) else Color(0xFFF1F1F1)
                )
            ) { // <--- La llave que abre el contenido de la tarjeta va aquí
                Text(
                    text = texto,
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Medium,
                    color = if (selectedOption == index) Color.White else Color.Black
                )
            }
        }

        // Este Spacer con weight(1f) empuja el botón al final de la pantalla
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBackToStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("VOLVER AL INICIO")
        }
    }
}