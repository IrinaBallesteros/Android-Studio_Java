package com.example.mipresupuestoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    EditText etIdentificacion, etContrasena, etRepetir, etPregunta, etRespuesta,
            etPrimerNombre, etSegundoNombre, etPrimerApellido, etSegundoApellido,
            etEmail, etTelefono, etPais, etCiudad;
    Spinner spTipoId, spGenero, spRol;
    Button btnGuardar, btnCancelar;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
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
        etSegundoNombre = findViewById(R.id.etSegundoNombre);
        etPrimerApellido = findViewById(R.id.etPrimerApellido);
        etSegundoApellido = findViewById(R.id.etSegundoApellido);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etPais = findViewById(R.id.etPais);
        etCiudad = findViewById(R.id.etCiudad);


        spTipoId = findViewById(R.id.spTipoId);
        spGenero = findViewById(R.id.spGenero);
        spRol = findViewById(R.id.spRol);


        spTipoId.setAdapter(getAdapter(R.array.tipo_identificacion_array));
        spGenero.setAdapter(getAdapter(R.array.genero_array));
        spRol.setAdapter(getAdapter(R.array.rol_array));


        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);


        btnGuardar.setOnClickListener(view -> {
            String id = etIdentificacion.getText().toString().trim();
            String pass = etContrasena.getText().toString().trim();
            String repetir = etRepetir.getText().toString().trim();


            if (id.isEmpty() || pass.isEmpty() || repetir.isEmpty() || !pass.equals(repetir)) {
                Toast.makeText(this, "Verifica los campos obligatorios y que las contraseñas coincidan", Toast.LENGTH_SHORT).show();
                return;
            }


            if (etPregunta.getText().toString().trim().isEmpty() ||
                    etRespuesta.getText().toString().trim().isEmpty() ||
                    etPrimerNombre.getText().toString().trim().isEmpty() ||
                    etPrimerApellido.getText().toString().trim().isEmpty() ||
                    etEmail.getText().toString().trim().isEmpty() ||
                    etPais.getText().toString().trim().isEmpty() ||
                    etCiudad.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios marcados con *", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbHelper.insertarUsuario(
                    id,
                    spTipoId.getSelectedItem().toString(),
                    pass,
                    etPregunta.getText().toString().trim(),
                    etRespuesta.getText().toString().trim(),
                    etPrimerNombre.getText().toString().trim(),
                    etSegundoNombre.getText().toString().trim(),
                    etPrimerApellido.getText().toString().trim(),
                    etSegundoApellido.getText().toString().trim(),
                    spGenero.getSelectedItem().toString(),
                    etEmail.getText().toString().trim(),
                    etTelefono.getText().toString().trim(),
                    spRol.getSelectedItem().toString(),
                    etPais.getText().toString().trim(),
                    etCiudad.getText().toString().trim(),
                    ""
            );

            if (exito) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Log.d("Registro", "Usuario guardado correctamente");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
                Log.e("Registro", "Fallo al insertar usuario");
            }
        });


        btnCancelar.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuPrincipalActivity.class));
            finish();
        });
    }


    private ArrayAdapter<CharSequence> getAdapter(int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}