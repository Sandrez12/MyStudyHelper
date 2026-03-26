package com.example.mystudyhelper.model

// Datos del estudiante 'Luis'
data class Estudiante(
    val nombre: String = "Luis",
    val carrera: String = "Ingeniería"
)

// Pregunta de Inglés (Estilo Duolingo)
data class Ejercicio(
    val pregunta: String = "¿Cómo se dice 'Hello' en español?",
    val opciones: List<String> = listOf("Hola", "Adiós", "Gracias"),
    val respuestaCorrecta: String = "Hola",
    val asignatura: String = "Inglés"
)

// Registro del evento de estrés para el historial
data class EventoEstres(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val intentosSalida: Int = 3,
    val estudiante: String = "Luis"
)