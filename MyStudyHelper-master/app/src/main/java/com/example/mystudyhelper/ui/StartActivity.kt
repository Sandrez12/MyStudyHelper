package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudyhelper.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnIrLogin = findViewById<Button>(R.id.btnIrLogin)
        val btnIrRegistro = findViewById<Button>(R.id.btnIrRegistro)

        // Si ya tiene cuenta, lo mandamos al Login
        btnIrLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Si es nuevo, lo mandamos al Registro (MainActivity)
        btnIrRegistro.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}