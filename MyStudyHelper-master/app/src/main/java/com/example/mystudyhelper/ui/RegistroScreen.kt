package com.example.mystudyhelper.ui

// Estos imports ahora sí funcionarán tras el Paso 1
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistroScreen(onNavigateNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MyStudyHelper",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )
        Text(text = "Registro de Sesión", color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        // Campo para 'Luis'
        OutlinedTextField(
            value = "Luis",
            onValueChange = {},
            label = { Text("Estudiante") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "Ingeniería de Sistemas",
            onValueChange = {},
            label = { Text("Carrera") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("COMENZAR EJERCICIO")
        }
    }
}