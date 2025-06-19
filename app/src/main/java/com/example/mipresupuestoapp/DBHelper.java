package com.example.mipresupuestoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MiPresupuesto.db";
    private static final int DATABASE_VERSION = 4;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "identificacion TEXT UNIQUE NOT NULL," +
                "tipo_Id TEXT NOT NULL," +
                "contrasena TEXT NOT NULL," +
                "pregunta TEXT NOT NULL," +
                "respuesta TEXT NOT NULL," +
                "primer_nombre TEXT NOT NULL," +
                "segundo_nombre TEXT," +
                "primer_apellido TEXT NOT NULL," +
                "segundo_apellido TEXT," +
                "genero TEXT," +
                "email TEXT NOT NULL," +
                "telefono TEXT," +
                "foto TEXT," +
                "rol TEXT NOT NULL," +
                "pais TEXT NOT NULL," +
                "ciudad TEXT NOT NULL," +
                "saldo REAL DEFAULT 0)");

        db.execSQL("CREATE TABLE ingresos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "identificacion TEXT NOT NULL," +
                "consecutivo INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "nombre TEXT NOT NULL," +
                "valor REAL NOT NULL," +
                "fuente TEXT," +
                "descripcion TEXT)");

        db.execSQL("CREATE TABLE gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "identificacion TEXT NOT NULL," +
                "consecutivo INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "nombre TEXT NOT NULL," +
                "valor REAL NOT NULL," +
                "categoria TEXT," +
                "descripcion TEXT)");

        db.execSQL("CREATE TABLE fuentes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE NOT NULL)");

        db.execSQL("CREATE TABLE categorias (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS ingresos");
        db.execSQL("DROP TABLE IF EXISTS gastos");
        db.execSQL("DROP TABLE IF EXISTS fuentes");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        onCreate(db);
    }


    public boolean insertarUsuario(String identificacion, String tipoId, String contrasena,
                                   String pregunta, String respuesta,
                                   String primerNombre, String segundoNombre,
                                   String primerApellido, String segundoApellido,
                                   String genero, String email, String telefono,
                                   String rol, String pais, String ciudad, String foto) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("identificacion", identificacion);
        values.put("tipo_Id", tipoId);
        values.put("contrasena", contrasena);
        values.put("pregunta", pregunta);
        values.put("respuesta", respuesta);
        values.put("primer_nombre", primerNombre);
        values.put("segundo_nombre", segundoNombre);
        values.put("primer_apellido", primerApellido);
        values.put("segundo_apellido", segundoApellido);
        values.put("genero", genero);
        values.put("email", email);
        values.put("telefono", telefono);
        values.put("rol", rol);
        values.put("pais", pais);
        values.put("ciudad", ciudad);
        values.put("foto", foto);

        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }
    public Cursor obtenerUsuarioPorId(String identificacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE identificacion = ?", new String[]{identificacion});
    }

    public boolean actualizarUsuario(String id, String telefono, String email, String contrasena, String pregunta, String respuesta, String pais, String ciudad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("telefono", telefono);
        valores.put("email", email);
        valores.put("contrasena", contrasena);
        valores.put("pregunta", pregunta);
        valores.put("respuesta", respuesta);
        valores.put("pais", pais);
        valores.put("ciudad", ciudad);
        int filas = db.update("usuarios", valores, "identificacion = ?", new String[]{id});
        return filas > 0;
    }

    public boolean eliminarUsuarioConDatos(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ingresos", "usuario_id = ?", new String[]{id});
        db.delete("gastos", "usuario_id = ?", new String[]{id});
        int filas = db.delete("usuarios", "identificacion = ?", new String[]{id});
        return filas > 0;
    }

    public Cursor obtenerUsuarioPorIdentificacion(String identificacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE identificacion = ?", new String[]{identificacion});
    }


    public Cursor validarUsuario(String identificacion, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE identificacion = ? AND contrasena = ?",
                new String[]{identificacion, contrasena});


    }


    public boolean insertarIngreso(String identificacionUsuario, String fecha, String nombre, double valor, String fuente, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();

        int consecutivo = obtenerNuevoConsecutivoIngreso(identificacionUsuario);

        ContentValues values = new ContentValues();
        values.put("identificacion", identificacionUsuario);
        values.put("consecutivo", consecutivo);
        values.put("fecha", fecha);
        values.put("nombre", nombre);
        values.put("valor", valor);
        values.put("fuente", fuente);
        values.put("descripcion", descripcion);

        long resultado = db.insert("ingresos", null, values);

        if (resultado != -1) {
            actualizarSaldoUsuario(identificacionUsuario, valor); // aumenta saldo
            return true;
        } else {
            return false;
        }
    }

    public double calcularSaldo(String identificacionUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cIngresos = db.rawQuery("SELECT SUM(valor) FROM ingresos WHERE identificacion = ?", new String[]{identificacionUsuario});
        Cursor cGastos = db.rawQuery("SELECT SUM(valor) FROM gastos WHERE identificacion = ?", new String[]{identificacionUsuario});

        double totalIngresos = 0;
        double totalGastos = 0;

        if (cIngresos.moveToFirst()) totalIngresos = cIngresos.getDouble(0);
        if (cGastos.moveToFirst()) totalGastos = cGastos.getDouble(0);

        cIngresos.close();
        cGastos.close();

        return totalIngresos - totalGastos;
    }

    private int obtenerNuevoConsecutivoIngreso(String identificacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(consecutivo) FROM ingresos WHERE identificacion = ?", new String[]{identificacion});
        int nuevo = 1;
        if (cursor.moveToFirst()) {
            int max = cursor.getInt(0);
            nuevo = max + 1;
        }
        cursor.close();
        return nuevo;
    }

    public long insertarGasto(String identificacion, String fecha, String nombre, double valor, String categoria, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();


        int nuevoConsecutivo = obtenerNuevoConsecutivoGasto(identificacion);

        ContentValues values = new ContentValues();
        values.put("identificacion", identificacion);
        values.put("consecutivo", nuevoConsecutivo);
        values.put("fecha", fecha);
        values.put("nombre", nombre);
        values.put("valor", valor);
        values.put("categoria", categoria);
        values.put("descripcion", descripcion);

        long id = db.insert("gastos", null, values);

        if (id != -1) {

            actualizarSaldoUsuario(identificacion, -valor);
        }

        return id;
    }


    public int obtenerNuevoConsecutivoGasto(String identificacion) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(consecutivo) FROM gastos WHERE identificacion = ?", new String[]{identificacion});
        int nuevo = 1;
        if (cursor.moveToFirst()) {
            int max = cursor.getInt(0);
            nuevo = max + 1;
        }
        cursor.close();
        return nuevo;
    }


    public void insertarCategoriaSiNoExiste(String categoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM categorias WHERE nombre = ?", new String[]{categoria});
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("nombre", categoria);
            db.insert("categorias", null, values);
        }
        cursor.close();
    }


    public boolean insertarFuente(String fuente) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM fuentes WHERE nombre = ?", new String[]{fuente});
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // Ya existe
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("nombre", fuente);

        long resultado = db.insert("fuentes", null, values);
        return resultado != -1;
    }

    public List<String> obtenerFuentes() {
        List<String> listaFuentes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre FROM fuentes ORDER BY nombre ASC", null);
        if (cursor.moveToFirst()) {
            do {
                listaFuentes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return listaFuentes;
    }

    public double recalcularSaldo(String identificacion) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cIngresos = db.rawQuery("SELECT SUM(valor) FROM ingresos WHERE identificacion = ?", new String[]{identificacion});
        Cursor cGastos = db.rawQuery("SELECT SUM(valor) FROM gastos WHERE identificacion = ?", new String[]{identificacion});

        double totalIngresos = 0;
        double totalGastos = 0;

        if (cIngresos.moveToFirst()) totalIngresos = cIngresos.getDouble(0);
        if (cGastos.moveToFirst()) totalGastos = cGastos.getDouble(0);

        cIngresos.close();
        cGastos.close();

        double nuevoSaldo = totalIngresos - totalGastos;


        SQLiteDatabase dbWritable = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("saldo", nuevoSaldo);
        dbWritable.update("usuarios", values, "identificacion = ?", new String[]{identificacion});

        return nuevoSaldo;
    }


    private void actualizarSaldoUsuario(String identificacion, double cambio) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT saldo FROM usuarios WHERE identificacion = ?", new String[]{identificacion});

        double saldoActual = 0;
        if (cursor.moveToFirst()) {
            saldoActual = cursor.getDouble(0);
        }
        cursor.close();

        double nuevoSaldo = saldoActual + cambio;

        ContentValues values = new ContentValues();
        values.put("saldo", nuevoSaldo);
        db.update("usuarios", values, "identificacion = ?", new String[]{identificacion});
    }


    public Cursor obtenerCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nombre FROM categorias ORDER BY nombre", null);
    }
}
