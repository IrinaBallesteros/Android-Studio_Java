package com.example.mipresupuestoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    TextView tvManual, tvVersion;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        tvManual = findViewById(R.id.tvManualUsuario);
        tvVersion = findViewById(R.id.tvVersion);


        tvVersion.setText("Versión 1.0");

        tvManual.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://irinaballesteros.github.io/mi_sitio/"));
            startActivity(browserIntent);
        });
    }
}