package com.example.mystudyhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystudyhelper.model.DataRepository
import com.example.mystudyhelper.model.EventoEstres

class MainViewModel : ViewModel() {
    // Instanciamos el Modelo (El repositorio que simula tu base de datos)
    private val repository = DataRepository()

    // Llevamos la cuenta de cuántas veces ha intentado salir
    private var exitAttempts = 0

    // Observable que la Vista vigilará. Si es 'true', muestra las opciones de descanso.
    private val _showStressOptions = MutableLiveData<Boolean>()
    val showStressOptions: LiveData<Boolean> get() = _showStressOptions

    // Función que la Vista llamará cada vez que el usuario presione "Salir"
    fun registerExitAttempt() {
        exitAttempts++

        // Regla de negocio del MVP: Si intenta salir 3 veces, consideramos que hay estrés
        if (exitAttempts >= 3) {

            // 1. Creamos el objeto usando tu data class.
            // Como ya tiene valores por defecto para el ID, fecha y estudiante ("Luis"),
            // solo actualizamos dinámicamente el número de intentos:
            val evento = EventoEstres(
                intentosSalida = exitAttempts
            )

            // 2. Se lo pasamos a la función para que lo guarde en la BD (simulada)
            val isSaved = repository.saveStressEvent(evento)

            // 3. Si se guardó, confirmamos el estrés y avisamos a la Vista
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