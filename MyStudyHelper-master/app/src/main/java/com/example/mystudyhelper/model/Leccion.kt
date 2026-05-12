package com.example.mystudyhelper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_lecciones")
data class Leccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String,
    val titulo: String,
    val contenidoTeorico: String,
    val ejemploPractico: String,
    val progreso: Int = 0
)