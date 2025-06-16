package com.example.mipresupuestoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public BaseDatos(Context context) {
        super(context, "presupuesto.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "identificacion TEXT," +
                "tipo_id TEXT," +
                "contrasena TEXT," +
                "pregunta TEXT," +
                "respuesta TEXT," +
                "nombre1 TEXT," +
                "nombre2 TEXT," +
                "apellido1 TEXT," +
                "apellido2 TEXT," +
                "genero TEXT," +
                "email TEXT," +
                "telefono TEXT," +
                "rol TEXT," +
                "pais TEXT," +
                "ciudad TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}