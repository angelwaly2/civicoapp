package co.civicoapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Negocio {

    public static List<List<String>> devolverDataTable(String SQL, Context c){
        List<List<String>> lista = new ArrayList<List<String>>();
        try{
            SQLite objc = new SQLite(c);
            objc.abrir();
            lista = objc.devolver_datatable(SQL);
            objc.cerrar();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return lista;
    }
    public static String listarString(String SQL, Activity mAct){
        String dato = "";
        SQLite objc = new SQLite(mAct);
        objc.abrir();
        dato = objc.listarString(SQL);
        objc.cerrar();
        return dato;
    }
    public static void ejecutarSQL(String SQL, Context c){
        try{
            SQLite objc = new SQLite(c);
            objc.abrir();
            objc.ejecutar(SQL);
            objc.cerrar();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public static void mostarProgreso(final boolean show, Context c, final View contenido, final View progreso) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = c.getResources().getInteger(android.R.integer.config_shortAnimTime);

            contenido.setVisibility(show ? View.GONE : View.VISIBLE);
            contenido.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    contenido.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progreso.setVisibility(show ? View.VISIBLE : View.GONE);
            progreso.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progreso.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progreso.setVisibility(show ? View.VISIBLE : View.GONE);
            contenido.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public static void mostrar_pop_up(Result result,Context c,View promptsView){
        LayoutInflater li = LayoutInflater.from(c);
        if (promptsView != null) {
            ViewGroup parent = (ViewGroup) promptsView.getParent();
            if (parent != null)
                parent.removeView(promptsView);
        }
        try {
            promptsView = li.inflate(R.layout.item_pop_up, null);
            TextView txtTitulo = (TextView) promptsView.findViewById(R.id.txtTitulo);
            txtTitulo.setText(result.name);
            TextView txtTelefono = (TextView) promptsView.findViewById(R.id.txtTelefono);
            txtTelefono.setText(result.phone_number);

        }catch (Exception e){}

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setCancelable(false).setPositiveButton("",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialogBuilder.setCancelable(true).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
