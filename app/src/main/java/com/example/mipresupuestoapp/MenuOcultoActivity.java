package com.example.mipresupuestoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuOcultoActivity extends AppCompatActivity {

    TextView tvBienvenida;
    Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_oculto);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        String identificacion = getIntent().getStringExtra("identificacion");
        if (identificacion != null) {
            tvBienvenida.setText("¡Bienvenida, " + identificacion + "!");
        }

        btnCerrarSesion.setOnClickListener(v -> {

            Intent intent = new Intent(MenuOcultoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
        });
    }
}

