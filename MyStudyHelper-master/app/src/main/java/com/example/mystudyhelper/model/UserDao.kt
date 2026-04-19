package com.example.mystudyhelper.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE correo = :email AND password = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): UserEntity?
}