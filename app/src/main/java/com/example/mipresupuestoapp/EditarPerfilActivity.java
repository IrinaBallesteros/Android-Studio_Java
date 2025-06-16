package com.example.mipresupuestoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class EditarPerfilActivity extends AppCompatActivity {

    EditText etTelefono, etEmail, etContrasena, etPregunta, etRespuesta, etPais, etCiudad;
    Button btnGuardar, btnEliminar;
    String identificacion;

    DBHelper dbHelper;
    Cursor datosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        etPregunta = findViewById(R.id.etPregunta);
        etRespuesta = findViewById(R.id.etRespuesta);
        etPais = findViewById(R.id.etPais);
        etCiudad = findViewById(R.id.etCiudad);
        btnGuardar = findViewById(R.id.btnGuardarCambios);
        btnEliminar = findViewById(R.id.btnEliminarCuenta);

        dbHelper = new DBHelper(this);


        identificacion = getIntent().getStringExtra("identificacion");

        if (identificacion != null) {
            datosUsuario = dbHelper.obtenerUsuarioPorId(identificacion);

            if (datosUsuario.moveToFirst()) {
                etTelefono.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("telefono")));
                etEmail.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("email")));
                etContrasena.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("contrasena")));
                etPregunta.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("pregunta")));
                etRespuesta.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("respuesta")));
                etPais.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("pais")));
                etCiudad.setText(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("ciudad")));
            }
        }

        btnGuardar.setOnClickListener(v -> guardarCambios());
        btnEliminar.setOnClickListener(v -> confirmarRespuestaParaEliminar());
    }

    private void guardarCambios() {
        String telefono = etTelefono.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();
        String pregunta = etPregunta.getText().toString().trim();
        String respuesta = etRespuesta.getText().toString().trim();
        String pais = etPais.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim();

        if (telefono.isEmpty() || email.isEmpty() || contrasena.isEmpty() || pregunta.isEmpty() || respuesta.isEmpty()
                || pais.isEmpty() || ciudad.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean actualizado = dbHelper.actualizarUsuario(identificacion, telefono, email, contrasena, pregunta, respuesta, pais, ciudad);

        if (actualizado) {
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarRespuestaParaEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar cuenta");

        final EditText input = new EditText(this);
        input.setHint("Ingresa tu respuesta secreta");
        builder.setView(input);

        builder.setPositiveButton("Verificar", (dialog, which) -> {
            String respuestaIngresada = input.getText().toString().trim();
            if (respuestaIngresada.equalsIgnoreCase(datosUsuario.getString(datosUsuario.getColumnIndexOrThrow("respuesta")))) {
                confirmarEliminacionFinal();
            } else {
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void confirmarEliminacionFinal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Eliminar cuenta definitivamente?");
        builder.setMessage("Esta acción eliminará todos tus datos, incluyendo ingresos y gastos.");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            boolean eliminado = dbHelper.eliminarUsuarioConDatos(identificacion);
            if (eliminado) {
                Toast.makeText(this, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
