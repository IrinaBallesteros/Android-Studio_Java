package com.example.mipresupuestoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FuentesActivity extends AppCompatActivity {

    EditText etNuevaFuente;
    Button btnAgregar;
    ListView lvFuentes;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    List<String> listaFuentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuentes);

        etNuevaFuente = findViewById(R.id.etNuevaFuente);
        btnAgregar = findViewById(R.id.btnAgregarFuente);
        lvFuentes = findViewById(R.id.lvFuentes);

        dbHelper = new DBHelper(this);
        cargarFuentes();

        btnAgregar.setOnClickListener(v -> {
            String fuente = etNuevaFuente.getText().toString().trim();
            if (!fuente.isEmpty()) {
                boolean exito = dbHelper.insertarFuente(fuente);
                if (exito) {
                    Toast.makeText(this, "Fuente agregada", Toast.LENGTH_SHORT).show();
                    etNuevaFuente.setText("");
                    cargarFuentes();
                } else {
                    Toast.makeText(this, "Ya existe esa fuente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvFuentes.setOnItemLongClickListener((parent, view, position, id) -> {
            String fuenteSeleccionada = listaFuentes.get(position);
            dbHelper.getWritableDatabase().delete("fuentes", "nombre = ?", new String[]{fuenteSeleccionada});
            cargarFuentes();
            Toast.makeText(this, "Fuente eliminada", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void cargarFuentes() {
        listaFuentes = dbHelper.obtenerFuentes();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaFuentes);
        lvFuentes.setAdapter(adapter);
    }
}