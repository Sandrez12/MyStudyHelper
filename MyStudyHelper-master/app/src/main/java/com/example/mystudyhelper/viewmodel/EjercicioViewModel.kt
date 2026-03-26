package com.example.mystudyhelper.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EjercicioViewModel : ViewModel() {private val _exitAttempts = MutableStateFlow(0)
    val exitAttempts: StateFlow<Int> = _exitAttempts.asStateFlow()

    private val _showStressAlert = MutableStateFlow(false)
    val showStressAlert: StateFlow<Boolean> = _showStressAlert.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    fun onExitClicked(onFinalExit: () -> Unit) {
        _exitAttempts.value += 1
        if (_exitAttempts.value == 1) _showStressAlert.value = true
        if (_exitAttempts.value >= 3) onFinalExit()
    }

    fun selectIntervention(index: Int) { _selectedOption.value = index }

    fun resetAll() {
        _exitAttempts.value = 0
        _showStressAlert.value = false
        _selectedOption.value = null
    }
}