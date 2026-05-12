package com.example.mystudyhelper.model


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Actualizamos la lista de entidades para incluir a Leccion
@Database(entities = [UserEntity::class, Leccion::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // El DAO que ya tenías para el inicio de sesión y registro
    abstract fun userDao(): UserDao

    // El nuevo DAO para el contenido de estudio universitario
    abstract fun leccionDao(): LeccionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "estudio_db" // Nombre de tu archivo de base de datos
                )
                    /* IMPORTANTE: fallbackToDestructiveMigration permite que, mientras desarrollas,
                       si cambias algo en las tablas, Room borre la versión vieja y cree la nueva
                       automáticamente en lugar de cerrar la app con un error de migración.
                    */
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}