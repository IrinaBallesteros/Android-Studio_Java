package com.example.mipresupuestoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    EditText etIdentificacion, etContrasena, etRepetir, etPregunta, etRespuesta, etPrimerNombre, etPrimerApellido, etEmail, etPais, etCiudad;
    Spinner spTipoId, spGenero, spRol;
    Button btnGuardar, btnCancelar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DBHelper(this);

        etIdentificacion = findViewById(R.id.etIdentificacion);
        etContrasena = findViewById(R.id.etContrasena);
        etRepetir = findViewById(R.id.etRepetirContrasena);
        etPregunta = findViewById(R.id.etPregunta);
        etRespuesta = findViewById(R.id.etRespuesta);
        etPrimerNombre = findViewById(R.id.etPrimerNombre);
        etPrimerApellido = findViewById(R.id.etPrimerApellido);
        etEmail = findViewById(R.id.etEmail);
        etPais = findViewById(R.id.etPais);
        etCiudad = findViewById(R.id.etCiudad);

        spTipoId = findViewById(R.id.spTipoIdentificacion);
        spGenero = findViewById(R.id.spGenero);
        spRol = findViewById(R.id.spRol);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(view -> {
            String id = etIdentificacion.getText().toString().trim();
            String pass = etContrasena.getText().toString().trim();
            String repetir = etRepetir.getText().toString().trim();

            if (id.isEmpty() || pass.isEmpty() || !pass.equals(repetir)) {
                Toast.makeText(this, "Verifica los campos obligatorios y que las contraseñas coincidan", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbHelper.insertarUsuario(
                    id,
                    spTipoId.getSelectedItem().toString(),
                    pass,
                    etPregunta.getText().toString().trim(),
                    etRespuesta.getText().toString().trim(),
                    etPrimerNombre.getText().toString().trim(),
                    "",
                    etPrimerApellido.getText().toString().trim(),
                    "",
                    spGenero.getSelectedItem().toString(),
                    etEmail.getText().toString().trim(),
                    "",
                    "",
                    spRol.getSelectedItem().toString(),
                    etPais.getText().toString().trim(),
                    etCiudad.getText().toString().trim()
            );

            if (exito) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
        });
    }
}