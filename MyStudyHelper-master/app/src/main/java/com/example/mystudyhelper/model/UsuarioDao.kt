package com.example.mystudyhelper.model

import androidx.room.*

@Dao
interface UsuarioDao {
    // Para el Registro: Guarda un usuario nuevo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: Usuario)

    // Para el Login: Busca un usuario por sus credenciales
    @Query("SELECT * FROM usuarios WHERE correo = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?

    // Para evitar correos duplicados:
    @Query("SELECT * FROM usuarios WHERE correo = :email LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(email: String): Usuario?

    // Para la Racha: Actualiza los datos del usuario cuando estudia
    @Update
    suspend fun actualizarUsuario(usuario: Usuario)
}