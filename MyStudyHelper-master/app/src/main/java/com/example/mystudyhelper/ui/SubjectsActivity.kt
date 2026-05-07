package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class SubjectsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)

        val cardMatematicas = findViewById<CardView>(R.id.cardMatematicas)
        val cardIngles = findViewById<CardView>(R.id.cardIngles)

        cardMatematicas.setOnClickListener {
            // Cuando tengamos MathMenuActivity lista, la lanzaremos aquí
            val intent = Intent(this, MathMenuActivity::class.java)
            startActivity(intent)
        }

        cardIngles.setOnClickListener {
            // Aquí irá el módulo de idiomas más adelante
        }
    }
}