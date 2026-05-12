package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class SubmateriasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submaterias)

        findViewById<ImageView>(R.id.btnRegresarSubmaterias).setOnClickListener { finish() }

        findViewById<CardView>(R.id.cardAritmetica).setOnClickListener { abrirTemario("Aritmética") }
        findViewById<CardView>(R.id.cardAlgebra).setOnClickListener { abrirTemario("Álgebra") }
        findViewById<CardView>(R.id.cardGeometria).setOnClickListener { abrirTemario("Geometría") }
    }

    private fun abrirTemario(nombreMateria: String) {
        val intent = Intent(this, LessonListActivity::class.java)
        // Le mandamos a la siguiente pantalla la materia que eligió el usuario
        intent.putExtra("MATERIA", nombreMateria)
        startActivity(intent)
    }
}