package com.example.mystudyhelper.model

import android.util.Log

class DataRepository {
    // Registra el evento de estrés detectado por el sensor lógico
    fun saveStressEvent(timestamp: Long, trigger: String): Boolean {
        // En una app real, aquí usarías Room o Firebase
        Log.d("SENSOR_LOGICO", "Evento guardado: $trigger a las $timestamp")
        return true
    }
}