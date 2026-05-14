package com.example.mystudyhelper.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystudyhelper.MainActivity // Importamos tu pantalla de registro
import com.example.mystudyhelper.R
import com.example.mystudyhelper.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnIrARegistro = findViewById<Button>(R.id.btnIrARegistro)

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(this)

            lifecycleScope.launch(Dispatchers.IO) {
                // CORRECCIÓN: usamos usuarioDao() con minúscula
                val usuario = db.usuarioDao().login(correo, pass)

                withContext(Dispatchers.Main) {
                    if (usuario != null) {
                        Toast.makeText(this@LoginActivity, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()

                        // Asegúrate de que SubjectsActivity exista, si no, usa la que tengas de menú
                        val intent = Intent(this@LoginActivity, SubjectsActivity::class.java)
                        intent.putExtra("USUARIO_ID", usuario.id)
                        startActivity(intent)
                        finish()
                    } else {
                        etCorreo.error = "Credenciales incorrectas"
                        Toast.makeText(this@LoginActivity, "El correo o la contraseña no coinciden", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        btnIrARegistro?.setOnClickListener {
            // CORRECCIÓN: Como tu registro es MainActivity, apuntamos ahí
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}