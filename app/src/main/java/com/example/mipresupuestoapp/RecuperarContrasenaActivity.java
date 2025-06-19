package com.example.mipresupuestoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    EditText etIdentificacion, etRespuesta, etNuevaContrasena;
    TextView tvPregunta;
    Button btnConsultar, btnRestablecer, btnCancelar;
    String respuestaCorrecta = "";
    DBHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        // Referencias de vistas
        etIdentificacion = findViewById(R.id.etIdentificacion);
        tvPregunta = findViewById(R.id.tvPregunta);
        etRespuesta = findViewById(R.id.etRespuesta);
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnRestablecer = findViewById(R.id.btnRestablecer);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBHelper(this);

        // Ocultar campos hasta consultar
        tvPregunta.setVisibility(View.GONE);
        etRespuesta.setVisibility(View.GONE);
        etNuevaContrasena.setVisibility(View.GONE);
        btnRestablecer.setVisibility(View.GONE);

        btnConsultar.setOnClickListener(v -> {
            String identificacion = etIdentificacion.getText().toString().trim();

            if (identificacion.isEmpty()) {
                Toast.makeText(this, "Ingrese el número de identificación", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = dbHelper.obtenerUsuarioPorIdentificacion(identificacion);

            if (cursor != null && cursor.moveToFirst()) {
                String pregunta = cursor.getString(cursor.getColumnIndexOrThrow("pregunta"));
                respuestaCorrecta = cursor.getString(cursor.getColumnIndexOrThrow("respuesta"));

                // Mostrar pregunta secreta
                tvPregunta.setText("Pregunta: " + pregunta);
                tvPregunta.setVisibility(View.VISIBLE);
                etRespuesta.setVisibility(View.VISIBLE);
                etNuevaContrasena.setVisibility(View.VISIBLE);
                btnRestablecer.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Ingresa la respuesta y una nueva contraseña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) cursor.close();
        });

        btnRestablecer.setOnClickListener(v -> {
            String identificacion = etIdentificacion.getText().toString().trim();
            String respuestaIngresada = etRespuesta.getText().toString().trim();
            String nuevaContrasena = etNuevaContrasena.getText().toString().trim();

            if (respuestaIngresada.isEmpty() || nuevaContrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (respuestaIngresada.equalsIgnoreCase(respuestaCorrecta)) {
                boolean actualizado = dbHelper.actualizarUsuario(
                        identificacion,
                        null,
                        null,
                        nuevaContrasena,
                        null,
                        null,
                        null,
                        null
                );

                if (actualizado) {
                    Toast.makeText(this, "Contraseña restablecida correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                etRespuesta.setText("");
            }
        });

        btnCancelar.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}