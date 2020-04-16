package co.civicoapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {

    SQLiteDatabase base_de_datos = this.getWritableDatabase();

    public SQLite(Context ctx) {
        super(ctx, "db_civico", (SQLiteDatabase.CursorFactory)null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS tbl_ultima_opcion(ultima_opcion VARCHAR(500));";
        db.execSQL(query);
    }
    public void ejecutar(String sql) {
        try {
            this.getWritableDatabase().execSQL(sql);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
    public List<List<String>> devolver_datatable(String SQL) {
        ArrayList lista = new ArrayList();
        Cursor c = this.base_de_datos.rawQuery(SQL, (String[])null);
        if(c != null && c.moveToFirst()) {
            do {
                try {
                    ArrayList exp = new ArrayList();
                    for(int i = 0; i < c.getColumnCount(); ++i) {
                        String dato = c.getString(i);
                        exp.add(dato);
                    }
                    lista.add(exp);
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            } while(c.moveToNext());
        }
        return lista;
    }
    public String listarString(String sql) {
        Cursor c = this.base_de_datos.rawQuery(sql, (String[])null);
        String str = "";
        if(c != null && c.moveToFirst()) {
            do {
                str = c.getString(0);
            } while(c.moveToNext());
        }
        this.cerrar();
        c.close();
        return str;
    }
    public void abrir() {
        this.getWritableDatabase();
    }
    public void cerrar() {
        this.close();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
