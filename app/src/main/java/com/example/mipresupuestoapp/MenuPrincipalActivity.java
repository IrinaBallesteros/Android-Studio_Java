package com.example.mipresupuestoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnIniciarSesion, btnRegistrarse, btnRecuperarContrasena, btnAcercaDe, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        TextView tv = findViewById(R.id.tvBienvenida);
        LinearLayout ly = findViewById(R.id.lySubmenu);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRecuperarContrasena = findViewById(R.id.btnRecuperarContrasena);
        btnAcercaDe = findViewById(R.id.btnAcercaDe);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);

        if (prefs.getBoolean("logueado", false)) {
            tv.setText("¡Bienvenida, " + prefs.getString("nombre", "Usuario") + "!");
            ly.setVisibility(View.VISIBLE);
            btnCerrarSesion.setVisibility(View.VISIBLE);  // Mostrar el botón si está logueado
        } else {
            tv.setText("Bienvenido(a) invitado");
            ly.setVisibility(View.GONE);
            btnCerrarSesion.setVisibility(View.GONE);
        }

        btnIniciarSesion.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, LoginActivity.class));
        });

        btnRegistrarse.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, RegistroActivity.class));
        });

        btnRecuperarContrasena.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, RecuperarContrasenaActivity.class));
        });

        btnAcercaDe.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, AcercaDeActivity.class));
        });

        btnCerrarSesion.setOnClickListener(view -> cerrarSesion());
    }

    private void cerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Elimina todos los datos del usuario
        editor.apply();

        Intent intent = new Intent(MenuPrincipalActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Borra la pila de actividades
        startActivity(intent);
    }
}
