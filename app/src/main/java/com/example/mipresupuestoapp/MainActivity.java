package com.example.mipresupuestoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Button btnIniciarSesion, btnRegistrarse;
    TextView tvAcercaDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvAcercaDe = findViewById(R.id.tvAcercaDe);

        btnIniciarSesion.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        btnRegistrarse.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistroActivity.class));
        });

        tvAcercaDe.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AcercaDeActivity.class));
        });
    }
}