package com.example.mipresupuestoapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InformeFinancieroActivity extends AppCompatActivity {

    TextView tvIngresos, tvGastos, tvSaldo;
    DBHelper dbHelper;
    String identificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_financiero);

        tvIngresos = findViewById(R.id.tvTotalIngresos);
        tvGastos = findViewById(R.id.tvTotalGastos);
        tvSaldo = findViewById(R.id.tvSaldoFinal);

        dbHelper = new DBHelper(this);

        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        identificacion = prefs.getString("identificacion", "");

        mostrarInforme();
    }

    private void mostrarInforme() {
        double ingresos = dbHelper.calcularSaldo(identificacion) + obtenerTotalGastos();
        double gastos = obtenerTotalGastos();
        double saldo = dbHelper.calcularSaldo(identificacion);

        tvIngresos.setText("Total Ingresos: $" + ingresos);
        tvGastos.setText("Total Gastos: $" + gastos);
        tvSaldo.setText("Saldo Final: $" + saldo);
    }

    private double obtenerTotalGastos() {
        double totalGastos = 0;
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT SUM(valor) FROM gastos WHERE identificacion = ?", new String[]{identificacion});
        if (c.moveToFirst()) totalGastos = c.getDouble(0);
        c.close();
        return totalGastos;
    }
}