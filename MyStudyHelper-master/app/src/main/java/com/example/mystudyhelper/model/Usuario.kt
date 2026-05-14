package com.example.mystudyhelper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val correo: String,
    val password: String,

    // Datos de la racha
    val rachaDias: Int = 0,
    val ultimaFechaEstudio: Long = 0
)