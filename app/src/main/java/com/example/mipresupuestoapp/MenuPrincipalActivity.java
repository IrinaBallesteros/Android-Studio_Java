package com.example.mipresupuestoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.mipresupuestoapp.FuentesActivity;
import com.example.mipresupuestoapp.CategoriasActivity;
import com.example.mipresupuestoapp.InformeFinancieroActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnIniciarSesion, btnRegistrarse, btnRecuperarContrasena, btnAcercaDe, btnCerrarSesion;
    private Button btnEditar, btnGestionarIngresos, btnGestionarGastos, btnGestionarFuentes, btnGestionarCategorias, btnInforme;
    private LinearLayout lySubmenu, lyInvitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        TextView tv = findViewById(R.id.tvBienvenida);
        lySubmenu = findViewById(R.id.lySubmenu);
        lyInvitado = findViewById(R.id.lyInvitado);


        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRecuperarContrasena = findViewById(R.id.btnRecuperarContrasena);


        btnEditar = findViewById(R.id.btnEditar);
        btnGestionarIngresos = findViewById(R.id.btnGestionarIngresos);
        btnGestionarGastos = findViewById(R.id.btnGestionarGastos);
        btnGestionarFuentes = findViewById(R.id.btnGestionarFuentes);
        btnGestionarCategorias = findViewById(R.id.btnGestionarCategorias);
        btnInforme = findViewById(R.id.btnInforme);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        Button btnGestionarFuentes = findViewById(R.id.btnGestionarFuentes);
        Button btnGestionarCategorias = findViewById(R.id.btnGestionarCategorias);
        Button btnInforme = findViewById(R.id.btnInforme);

        btnAcercaDe = findViewById(R.id.btnAcercaDe);

        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);

        boolean logueado = prefs.getBoolean("logueado", false);
        String nombre = prefs.getString("nombre", "Usuario");

        if (logueado) {
            tv.setText("¡Bienvenida, " + nombre + "!");
            lySubmenu.setVisibility(View.VISIBLE);
            lyInvitado.setVisibility(View.GONE);
        } else {
            tv.setText("Bienvenido(a) invitado");
            lySubmenu.setVisibility(View.GONE);
            lyInvitado.setVisibility(View.VISIBLE);
        }


        btnIniciarSesion.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnRegistrarse.setOnClickListener(v -> startActivity(new Intent(this, RegistroActivity.class)));
        btnRecuperarContrasena.setOnClickListener(v -> startActivity(new Intent(this, RecuperarContrasenaActivity.class)));


        btnEditar.setOnClickListener(v -> startActivity(new Intent(this, EditarPerfilActivity.class)));
        btnGestionarIngresos.setOnClickListener(v -> startActivity(new Intent(this, GestionarIngresoActivity.class)));
        btnGestionarGastos.setOnClickListener(v -> startActivity(new Intent(this, GestionarGastoActivity.class)));
        btnGestionarFuentes.setOnClickListener(v -> startActivity(new Intent(this, FuentesActivity.class)));
        btnGestionarCategorias.setOnClickListener(v -> startActivity(new Intent(this, CategoriasActivity.class)));
        btnInforme.setOnClickListener(v -> startActivity(new Intent(this, InformeFinancieroActivity.class)));

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
        btnGestionarFuentes.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, FuentesActivity.class));
        });

        btnGestionarCategorias.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, CategoriasActivity.class));
        });

        btnInforme.setOnClickListener(view -> {
            startActivity(new Intent(MenuPrincipalActivity.this, InformeFinancieroActivity.class));
        });
        btnAcercaDe.setOnClickListener(v -> startActivity(new Intent(this, AcercaDeActivity.class)));
    }

    private void cerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}