package com.example.tareakoko;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class InicioActivity extends AppCompatActivity {

    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Inicializar KeyguardManager
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        // Botón para gestionar las contraseñas
        MaterialButton gestionarClavesButton = findViewById(R.id.gestionarClavesButton);
        gestionarClavesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el dispositivo tiene una forma de seguridad habilitada
                if (keyguardManager.isKeyguardSecure()) {
                    // Si la seguridad está habilitada, solicitar la autenticación del usuario
                    Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("Autenticación requerida", "Por favor, verifique su identidad");
                    startActivityForResult(intent, 1);  // El código de solicitud es 1
                } else {
                    // Si no hay seguridad configurada, simplemente abrir la actividad
                    abrirGestionarClaves();
                }
            }
        });

        // Botón para cerrar la sesion
        MaterialButton cerrarSesionButton = findViewById(R.id.cerrarSesionButton);
        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad principal (inicio de sesión)
                Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Finalizar LobbyActivity para que no pueda volver con el botón de atrás
            }
        });

        // Botón para agregar la contraseña
        MaterialButton agregarClaveButton = findViewById(R.id.agregarClaveButton);
        agregarClaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad para crear una nueva clave
                Intent intent = new Intent(InicioActivity.this, CrearContraseñaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Si el patrón fue correcto, abrir la actividad de gestionar claves
                abrirGestionarClaves();
            } else {
                // Si la autenticación falla
                Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // funcion para abrir la actividad de gestionar contraseñas
    private void abrirGestionarClaves() {
        Intent intent = new Intent(InicioActivity.this, VerContraseñasActivity.class);
        startActivity(intent);
    }
}

