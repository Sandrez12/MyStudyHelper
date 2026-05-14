package com.example.mystudyhelper.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Cambiamos UserEntity por Usuario
@Database(entities = [Usuario::class, Leccion::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. Cambiamos userDao() por usuarioDao() para que coincida con tu LoginActivity
    abstract fun usuarioDao(): UsuarioDao

    abstract fun leccionDao(): LeccionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "estudio_db"
                )
                    // Subimos la versión a 5 y mantenemos el fallback para evitar errores de migración
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}