package com.example.mipresupuestoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etIdentificacionLogin, etContrasenaLogin;
    Button btnIniciarSesion, btnRegistrarse, btnRecuperarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etIdentificacionLogin = findViewById(R.id.etIdentificacionLogin);
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRecuperarContrasena = findViewById(R.id.btnRecordar);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        btnRecuperarContrasena.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String identificacion = etIdentificacionLogin.getText().toString().trim();
        String contrasena = etContrasenaLogin.getText().toString().trim();

        if (identificacion.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE identificacion = ? AND contrasena = ?",
                new String[]{identificacion, contrasena}
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("primer_nombre"));

            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();


            SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("logueado", true);
            editor.putString("nombre", nombre);
            editor.putString("identificacion", identificacion);
            editor.apply();

            Intent intent = new Intent(this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}