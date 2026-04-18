package com.example.mystudyhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystudyhelper.model.DataRepository

class MainViewModel : ViewModel() {
    private val repository = DataRepository()

    // Configuración del sensor: 3 intentos en una ventana de 5 segundos
    private val MAX_ATTEMPTS = 3
    private val TIME_WINDOW = 5000L

    private var attemptCount = 0
    private var firstAttemptTime = 0L

    private val _showIntervention = MutableLiveData<Boolean>()
    val showIntervention: LiveData<Boolean> get() = _showIntervention

    fun onExitAttempt() {
        val currentTime = System.currentTimeMillis()

        // Si pasó mucho tiempo o es el primer intento, reiniciamos el sensor
        if (attemptCount == 0 || currentTime - firstAttemptTime > TIME_WINDOW) {
            attemptCount = 1
            firstAttemptTime = currentTime
            _showIntervention.value = false
        } else {
            attemptCount++
            // Si llega al umbral dentro del tiempo, disparamos la intervención
            if (attemptCount >= MAX_ATTEMPTS) {
                if (repository.saveStressEvent(currentTime, "Gestos rápidos de salida")) {
                    _showIntervention.value = true
                    attemptCount = 0 // Reiniciamos para no repetir la alerta de golpe
                }
            }
        }
    }
    private val _showPermissionsDialog = MutableLiveData<Boolean>()
    val showPermissionsDialog: LiveData<Boolean> get() = _showPermissionsDialog


    fun onRegisterClicked() {
        _showPermissionsDialog.value = true
    }

    // Esta función se llama si el usuario acepta ir a configuración
    fun onPermissionsConfirmed() {
        _showPermissionsDialog.value = false
    }

    // Esta función se llama si el usuario elige "Ahora no"
    fun onPermissionsDismissed() {
        _showPermissionsDialog.value = false
    }
}
