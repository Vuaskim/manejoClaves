package com.example.tareakoko;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class VerContraseñasActivity extends AppCompatActivity {

    private ListView listaClaves;
    private Button botonModificarClave;
    private Button botonEliminarClave;
    private KeyguardManager keyguardManager;
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private String selectedClaveId;
    private Runnable pendingAction;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_contrasena);

        // Asignación de las IDs desde el layout
        listaClaves = findViewById(R.id.listaClaves);
        botonModificarClave = findViewById(R.id.botonModificarClave);
        botonEliminarClave = findViewById(R.id.botonEliminarClave);
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        // Obtener la referencia de Firebase para el usuario actual
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userUid);

        cargarClaves();

        // Acción de modificar las contraseñas
        botonModificarClave.setOnClickListener(v -> verifyDeviceSecurity(() -> {
            Intent intent = new Intent(VerContraseñasActivity.this, ModificarContraseñaActivity.class);
            intent.putExtra("claveId", selectedClaveId);
            startActivity(intent);
        }));

        // Acción de eliminar clave
        botonEliminarClave.setOnClickListener(v -> verifyDeviceSecurity(this::showDeleteConfirmationDialog));
    }

    // funcion para cargar contraseñas desde Firebase
    private void cargarClaves() {
        databaseReference.child("claves").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Contraseñas> clavesList = new ArrayList<>();
                ArrayList<String> claveIds = new ArrayList<>();

                for (DataSnapshot claveSnapshot : snapshot.getChildren()) {
                    claveIds.add(claveSnapshot.getKey());
                    String clave = claveSnapshot.child("clave").getValue(String.class);
                    String userName = claveSnapshot.child("userName").getValue(String.class);
                    clavesList.add(new Contraseñas(userName, clave));
                }

                ContraseñasAdapter adapter = new ContraseñasAdapter(VerContraseñasActivity.this, clavesList);
                listaClaves.setAdapter(adapter);

                listaClaves.setOnItemClickListener((parent, view, position, id) -> {
                    selectedClaveId = claveIds.get(position);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerContraseñasActivity.this, "Error al cargar claves", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Verificar la seguridad del dispositivo (keyguard)
    private void verifyDeviceSecurity(Runnable onSuccess) {
        if (keyguardManager.isKeyguardSecure()) {
            pendingAction = onSuccess;
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("Autenticación requerida", "Verifique su identidad");
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        } else {
            Toast.makeText(this, "La seguridad del dispositivo no está habilitada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS && resultCode == Activity.RESULT_OK) {
            if (pendingAction != null) {
                pendingAction.run();
                pendingAction = null;
            }
        } else {
            Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
        }
    }

    // Mostrar diálogo de confirmación antes de eliminar las contraseñas
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta clave?")
                .setPositiveButton("Sí", (dialog, which) -> deleteClave())
                .setNegativeButton("No", null)
                .show();
    }

    // Metodo para eliminar contraseñas en Firebase
    private void deleteClave() {
        databaseReference.child("claves").child(selectedClaveId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Clave eliminada con éxito", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar la clave", Toast.LENGTH_SHORT).show());
    }
}
