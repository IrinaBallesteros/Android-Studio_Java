package com.example.mipresupuestoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    EditText etIdentificacion, etRespuesta;
    TextView tvPregunta;
    Button btnConsultar, btnCancelar;
    String respuestaCorrecta = "", contrasenaRecuperada = "", emailUsuario = "";
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        etIdentificacion = findViewById(R.id.etIdentificacion);
        tvPregunta = findViewById(R.id.tvPregunta);
        etRespuesta = findViewById(R.id.etRespuesta);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new DBHelper(this);
        etRespuesta.setVisibility(View.GONE);
        tvPregunta.setVisibility(View.GONE);

        btnConsultar.setOnClickListener(view -> {
            String identificacion = etIdentificacion.getText().toString().trim();

            if (respuestaCorrecta.isEmpty()) {

                if (!identificacion.isEmpty()) {
                    Cursor cursor = dbHelper.obtenerUsuarioPorIdentificacion(identificacion);
                    if (cursor != null && cursor.moveToFirst()) {
                        String pregunta = cursor.getString(cursor.getColumnIndexOrThrow("pregunta"));
                        respuestaCorrecta = cursor.getString(cursor.getColumnIndexOrThrow("respuesta"));
                        contrasenaRecuperada = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));
                        emailUsuario = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                        tvPregunta.setText("Pregunta: " + pregunta);
                        tvPregunta.setVisibility(View.VISIBLE);
                        etRespuesta.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Ingresa la respuesta para continuar", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Ingrese el número de identificación", Toast.LENGTH_SHORT).show();
                }
            } else {
                String respuestaIngresada = etRespuesta.getText().toString().trim();
                if (respuestaIngresada.equalsIgnoreCase(respuestaCorrecta)) {

                    Toast.makeText(this, "Contraseña: " + contrasenaRecuperada +
                            "\nEnviada a: " + emailUsuario, Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(RecuperarContrasenaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Respuesta incorrecta. Intente de nuevo.", Toast.LENGTH_SHORT).show();
                    respuestaCorrecta = ""; // Resetear el flujo
                    etRespuesta.setText("");
                    etRespuesta.setVisibility(View.GONE);
                    tvPregunta.setVisibility(View.GONE);
                }
            }
        });

        btnCancelar.setOnClickListener(view -> {
            Intent intent = new Intent(RecuperarContrasenaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}