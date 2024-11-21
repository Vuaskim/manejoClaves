package com.example.tareakoko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);

            // Configurar padding para barras del sistema
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Referencias a los botones
            MaterialButton loginButton = findViewById(R.id.btnInicioSesion);
            MaterialButton registerButton = findViewById(R.id.btnRegistrar);

            // Listener para el botón de inicio de sesión
            if (loginButton != null) {
                loginButton.setOnClickListener(v -> {
                    Intent loginIntent = new Intent(MainActivity.this, InicioActivity.class);
                    startActivity(loginIntent);
                });
            } else {
                Log.e("MainActivity", "El botón de inicio de sesión no se encontró.");
            }

            // Listener para el botón de registro
            if (registerButton != null) {
                registerButton.setOnClickListener(v -> {
                    Intent registerIntent = new Intent(MainActivity.this, RegistrarActivity.class);
                    startActivity(registerIntent);
                });
            } else {
                Log.e("MainActivity", "El botón de registro no se encontró.");
            }

        } catch (Exception e) {
            Log.e("MainActivity", "Error en onCreate: ", e);
        }
    }
}
