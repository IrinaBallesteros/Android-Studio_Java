package com.example.mipresupuestoapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class GestionarIngresoActivity extends AppCompatActivity {

    EditText etFecha, etNombre, etValor, etDescripcion;
    Spinner spFuente;
    Button btnGuardar, btnCancelar, btnAgregarFuente;
    DBHelper dbHelper;
    String identificacionUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_ingreso);


        etFecha = findViewById(R.id.etFecha);
        etNombre = findViewById(R.id.etNombreIngreso);
        etValor = findViewById(R.id.etValor);
        etDescripcion = findViewById(R.id.etDescripcion);
        spFuente = findViewById(R.id.spFuente);
        btnGuardar = findViewById(R.id.btnGuardarIngreso);
        btnCancelar = findViewById(R.id.btnCancelarIngreso);
        btnAgregarFuente = findViewById(R.id.btnAgregarFuente);

        dbHelper = new DBHelper(this);


        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        identificacionUsuario = prefs.getString("identificacion", "");


        etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));


        cargarFuentes();


        etFecha.setOnClickListener(v -> mostrarSelectorFecha());
        btnGuardar.setOnClickListener(v -> guardarIngreso());
        btnCancelar.setOnClickListener(v -> finish());
        btnAgregarFuente.setOnClickListener(v -> agregarFuente());
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String fechaFormateada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
            etFecha.setText(fechaFormateada);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void cargarFuentes() {
        List<String> fuentes = dbHelper.obtenerFuentes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuentes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFuente.setAdapter(adapter);
    }

    private void agregarFuente() {
        EditText input = new EditText(this);
        new android.app.AlertDialog.Builder(this)
                .setTitle("Agregar nueva fuente")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevaFuente = input.getText().toString().trim();
                    if (!nuevaFuente.isEmpty()) {
                        dbHelper.insertarFuente(nuevaFuente);
                        cargarFuentes();
                        Toast.makeText(this, "Fuente agregada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Nombre de fuente vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarIngreso() {
        String fecha = etFecha.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String valorStr = etValor.getText().toString().trim();
        String fuente = spFuente.getSelectedItem() != null ? spFuente.getSelectedItem().toString() : "";
        String descripcion = etDescripcion.getText().toString().trim();

        if (fecha.isEmpty() || nombre.isEmpty() || valorStr.isEmpty() || fuente.isEmpty()) {
            Toast.makeText(this, "Todos los campos obligatorios deben estar completos", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean insertado = dbHelper.insertarIngreso(identificacionUsuario, fecha, nombre, valor, fuente, descripcion);

        if (insertado) {
            double saldoActual = dbHelper.calcularSaldo(identificacionUsuario);
            Toast.makeText(this, "Ingreso registrado. Nuevo saldo: $" + saldoActual, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar el ingreso", Toast.LENGTH_SHORT).show();
        }
    }
}