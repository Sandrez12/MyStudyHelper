package com.example.mystudyhelper // Asegúrate de que este sea tu paquete real

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystudyhelper.R
import com.example.mystudyhelper.model.AppDatabase
import com.example.mystudyhelper.model.Usuario // Asegúrate de importar tu nueva clase
import com.example.mystudyhelper.ui.LoginActivity
import com.example.mystudyhelper.ui.SubjectsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vinculamos los IDs del XML (Confirma que existan en activity_main.xml)
        val etCorreo = findViewById<EditText>(R.id.etRegistroCorreo)
        val etPass = findViewById<EditText>(R.id.etRegistroPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(this)

            lifecycleScope.launch(Dispatchers.IO) {
                // Cambiamos a la nueva función del DAO
                val existe = db.usuarioDao().obtenerUsuarioPorCorreo(correo)

                withContext(Dispatchers.Main) {
                    if (existe != null) {
                        etCorreo.error = "Este correo ya está registrado"
                    } else {
                        registrarNuevoUsuario(db, correo, pass)
                    }
                }
            }
        }
    }

    private fun registrarNuevoUsuario(db: AppDatabase, correo: String, pass: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Creamos el objeto Usuario con los nuevos campos de racha
            val nuevo = Usuario(
                correo = correo,
                password = pass,
                rachaDias = 1,
                ultimaFechaEstudio = System.currentTimeMillis()
            )

            db.usuarioDao().insertarUsuario(nuevo)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "¡Cuenta creada! Racha: 1 🔥", Toast.LENGTH_LONG).show()
                // Mandamos al Login después del registro
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}