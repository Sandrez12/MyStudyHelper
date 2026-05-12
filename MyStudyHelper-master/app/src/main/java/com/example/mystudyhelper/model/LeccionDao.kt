package com.example.mystudyhelper.model
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystudyhelper.model.Leccion

@Dao
interface LeccionDao {
    @Query("SELECT * FROM tabla_lecciones WHERE materia = :nombreMateria")
    suspend fun obtenerLeccionesPorMateria(nombreMateria: String): List<Leccion>

    @Query("UPDATE tabla_lecciones SET progreso = :nuevoProgreso WHERE id = :idLeccion")
    suspend fun actualizarProgreso(idLeccion: Int, nuevoProgreso: Int)

    @Query("SELECT * FROM tabla_lecciones WHERE id = :leccionId")
    suspend fun obtenerLeccionPorId(leccionId: Int): Leccion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLecciones(lecciones: List<Leccion>)
}