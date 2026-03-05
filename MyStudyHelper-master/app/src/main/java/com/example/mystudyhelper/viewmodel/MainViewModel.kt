package com.example.mystudyhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystudyhelper.model.DataRepository

class MainViewModel : ViewModel() {
    // Instanciamos el Modelo
    private val repository = DataRepository()

    // Llevamos la cuenta de cuántas veces ha intentado salir
    private var exitAttempts = 0

    // Observable que la Vista vigilará. Si es 'true', muestra las opciones de descanso.
    private val _showStressOptions = MutableLiveData<Boolean>()
    val showStressOptions: LiveData<Boolean> get() = _showStressOptions

    // Función que la Vista llamará cada vez que el usuario presione "Salir"
    fun registerExitAttempt() {
        exitAttempts++

        // Regla de negocio: Si intenta salir 3 veces, consideramos que hay estrés
        if (exitAttempts >= 3) {
            // 1. Notificamos al modelo para que lo guarde en la BD (simulado)
            val isSaved = repository.saveStressEvent()

            // 2. Si se guardó, confirmamos el estrés y avisamos a la Vista
            if (isSaved) {
                _showStressOptions.value = true
            }
        }
    }

    // Función extra para reiniciar el contador cuando el usuario ya haya descansado
    fun resetState() {
        exitAttempts = 0
        _showStressOptions.value = false
    }
}