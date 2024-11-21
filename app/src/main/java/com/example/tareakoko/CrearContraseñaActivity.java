package com.example.tareakoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearContraseñaActivity extends AppCompatActivity {

    // Declarar las vistas
    private TextInputEditText origenInput, claveInput, nombreUsuarioInput; // Nombres adaptados al XML
    private MaterialButton botonGuardar; // Botón adaptado al XML
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_contrasena);

        // Inicializar las vistas usando findViewById
        origenInput = findViewById(R.id.origenInput); // Campo de lugar de origen
        claveInput = findViewById(R.id.claveInput); // Campo de clave
        nombreUsuarioInput = findViewById(R.id.nombreUsuarioInput); // Campo de apodo
        botonGuardar = findViewById(R.id.botonGuardar); // Botón de guardar

        // Inicializar Firebase Auth y Realtime Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Configurar el listener del botón de guardar
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        // Obtener los valores de los campos de entrada
        String origen = origenInput.getText().toString().trim();
        String clave = claveInput.getText().toString().trim();
        String apodo = nombreUsuarioInput.getText().toString().trim(); // Obtener el valor del apodo

        // Validar que los campos no estén vacíos
        if (origen.isEmpty() || clave.isEmpty() || apodo.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el UID del usuario autenticado
        String userId = auth.getCurrentUser().getUid();

        // Crear un mapa para los datos
        Map<String, String> data = new HashMap<>();
        data.put("origen", origen);
        data.put("clave", clave);
        data.put("apodo", apodo); // Añadir el apodo a los datos

        // Obtener la referencia de usuario y claves
        DatabaseReference userClavesRef = databaseReference.child(userId).child("claves");

        // Obtener las claves actuales
        userClavesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si ya existen claves, agregar a la lista
                List<Map<String, String>> clavesList = new ArrayList<>();
                if (task.getResult().exists()) {
                    // Si ya existen claves, obtenemos la lista actual
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        clavesList.add((Map<String, String>) snapshot.getValue());
                    }
                }

                // Agregar la nueva clave a la lista
                clavesList.add(data);

                // Guardar todas las claves (nueva y anteriores)
                userClavesRef.setValue(clavesList).addOnCompleteListener(saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Toast.makeText(CrearContraseñaActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                        // Redirigir a la actividad LobbyActivity
                        Intent intent = new Intent(CrearContraseñaActivity.this, InicioActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar la actividad actual para que no quede en el back stack
                    } else {
                        Toast.makeText(CrearContraseñaActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(CrearContraseñaActivity.this, "Error al obtener las claves", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
