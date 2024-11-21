package com.example.tareakoko;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesionActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private MaterialButton loginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);  // Cambia a activity_inicio_sesion.xml

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Inicializar vistas
        emailField = findViewById(R.id.emailInput);  // Corresponde al EditText para el correo
        passwordField = findViewById(R.id.passwordInput);  // Corresponde al EditText para la clave
        loginButton = findViewById(R.id.loginButton);  // Corresponde al botón de inicio de sesión

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Ingresa tu correo");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Ingresa tu clave");
            return;
        }

        // Autenticar con Firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, redirigir a la actividad lobby
                        FirebaseUser user = auth.getCurrentUser();
                        Intent intent = new Intent(InicioSesionActivity.this, InicioActivity.class);  // Cambiar a la actividad que corresponda
                        startActivity(intent);
                        finish();
                    } else {
                        // Error en el inicio de sesión, mostrar mensaje de error
                        Toast.makeText(InicioSesionActivity.this, "Credenciales incorrectas o no registradas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
