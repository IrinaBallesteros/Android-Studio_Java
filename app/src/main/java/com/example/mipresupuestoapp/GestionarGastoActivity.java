package com.example.mipresupuestoapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class GestionarGastoActivity extends AppCompatActivity {

    EditText etFechaGasto, etNombreGasto, etValorGasto, etDescripcionGasto, etNuevaCategoria;
    Spinner spinnerCategoriaGasto;
    Button btnGuardarGasto, btnCancelarGasto;

    String identificacionUsuario;
    ArrayList<String> listaCategorias = new ArrayList<>();
    ArrayAdapter<String> adaptadorCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_gasto);

        etFechaGasto = findViewById(R.id.etFechaGasto);
        etNombreGasto = findViewById(R.id.etNombreGasto);
        etValorGasto = findViewById(R.id.etValorGasto);
        etDescripcionGasto = findViewById(R.id.etDescripcionGasto);
        etNuevaCategoria = findViewById(R.id.etNuevaCategoria);
        spinnerCategoriaGasto = findViewById(R.id.spinnerCategoriaGasto);
        btnGuardarGasto = findViewById(R.id.btnGuardarGasto);
        btnCancelarGasto = findViewById(R.id.btnCancelarGasto);


        identificacionUsuario = getIntent().getStringExtra("identificacion");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etFechaGasto.setText(sdf.format(new Date()));

        etFechaGasto.setOnClickListener(v -> mostrarSelectorFecha());

        cargarCategorias();

        btnGuardarGasto.setOnClickListener(v -> guardarGasto());
        btnCancelarGasto.setOnClickListener(v -> finish());
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
                    etFechaGasto.setText(fecha);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void cargarCategorias() {
        listaCategorias.clear();
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.obtenerCategorias();

        if (cursor.moveToFirst()) {
            do {
                listaCategorias.add(cursor.getString(0)); // columna nombre
            } while (cursor.moveToNext());
        }

        cursor.close();

        adaptadorCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCategorias);
        adaptadorCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaGasto.setAdapter(adaptadorCategorias);
    }

    private void guardarGasto() {
        String fecha = etFechaGasto.getText().toString().trim();
        String nombre = etNombreGasto.getText().toString().trim();
        String valorStr = etValorGasto.getText().toString().trim();
        String descripcion = etDescripcionGasto.getText().toString().trim();
        String nuevaCategoria = etNuevaCategoria.getText().toString().trim();
        String categoria = nuevaCategoria.isEmpty() ? spinnerCategoriaGasto.getSelectedItem().toString() : nuevaCategoria;

        if (fecha.isEmpty() || nombre.isEmpty() || valorStr.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                Toast.makeText(this, "El valor debe ser mayor que 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        long idGasto = dbHelper.insertarGasto(identificacionUsuario, fecha, nombre, valor, categoria, descripcion);

        if (!nuevaCategoria.isEmpty()) {
            dbHelper.insertarCategoriaSiNoExiste(nuevaCategoria);
            cargarCategorias(); // Recargar con nueva categoría incluida
        }

        if (idGasto > 0) {
            double nuevoSaldo = dbHelper.recalcularSaldo(identificacionUsuario);
            Toast.makeText(this, "Gasto registrado. Nuevo saldo: $" + nuevoSaldo, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el gasto", Toast.LENGTH_SHORT).show();
        }
    }
}
