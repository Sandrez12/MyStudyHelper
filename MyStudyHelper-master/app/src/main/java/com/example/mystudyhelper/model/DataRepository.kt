package com.example.mystudyhelper.model

class DataRepository {
    // Ahora recibe el objeto del evento
    fun saveStressEvent(evento: EventoEstres): Boolean {
        println("Evento guardado: ${evento.id} para el estudiante ${evento.estudiante}")
        return true
    }
}