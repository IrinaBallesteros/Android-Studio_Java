package com.example.mipresupuestoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    EditText etNuevaCategoria;
    Button btnAgregar;
    ListView lvCategorias;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    List<String> listaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        etNuevaCategoria = findViewById(R.id.etNuevaCategoria);
        btnAgregar = findViewById(R.id.btnAgregarCategoria);
        lvCategorias = findViewById(R.id.lvCategorias);

        dbHelper = new DBHelper(this);
        cargarCategorias();

        btnAgregar.setOnClickListener(v -> {
            String categoria = etNuevaCategoria.getText().toString().trim();
            if (!categoria.isEmpty()) {
                dbHelper.insertarCategoriaSiNoExiste(categoria);
                Toast.makeText(this, "Categoría agregada", Toast.LENGTH_SHORT).show();
                etNuevaCategoria.setText("");
                cargarCategorias();
            }
        });

        lvCategorias.setOnItemLongClickListener((parent, view, position, id) -> {
            String categoriaSeleccionada = listaCategorias.get(position);
            dbHelper.getWritableDatabase().delete("categorias", "nombre = ?", new String[]{categoriaSeleccionada});
            cargarCategorias();
            Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void cargarCategorias() {
        listaCategorias = new ArrayList<>();
        Cursor cursor = dbHelper.obtenerCategorias();
        if (cursor.moveToFirst()) {
            do {
                listaCategorias.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCategorias);
        lvCategorias.setAdapter(adapter);
    }
}