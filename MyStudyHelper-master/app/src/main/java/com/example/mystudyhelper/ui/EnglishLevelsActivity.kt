package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mystudyhelper.R

class EnglishLevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_english_levels)

        findViewById<ImageView>(R.id.btnRegresarEnglish).setOnClickListener { finish() }

        findViewById<CardView>(R.id.cardA1).setOnClickListener { abrirTemario("A1") }
        findViewById<CardView>(R.id.cardA2).setOnClickListener { abrirTemario("A2") }
        findViewById<CardView>(R.id.cardB1).setOnClickListener { abrirTemario("B1") }
        findViewById<CardView>(R.id.cardB2).setOnClickListener { abrirTemario("B2") }
        findViewById<CardView>(R.id.cardC1).setOnClickListener { abrirTemario("C1") }
    }

    private fun abrirTemario(nivel: String) {
        val intent = Intent(this, LessonListActivity::class.java)
        intent.putExtra("MATERIA", nivel)
        startActivity(intent)
    }
}