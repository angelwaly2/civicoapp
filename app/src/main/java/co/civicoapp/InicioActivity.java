package co.civicoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class InicioActivity extends AppCompatActivity {

    /***** OBJECTS ***/
    Boolean habilitado = null;
    Localizacion miLocalizador;
    Spinner spnTermino;
    Button btnIngresar;
    /****************/
    /**todo=Método de creación*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        // Obtener la posicion del dispositivo
        inicioVariablesObjectos();
        inicioThreadGPS();
    }
    private void inicioVariablesObjectos(){
        spnTermino = (Spinner) findViewById(R.id.spnTermino);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.terminos, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTermino.setAdapter(adaptador);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avanzar();
            }
        });
        btnIngresar.setEnabled(false);
    }
    private void avanzar(){
        Location loc = miLocalizador.loc;
        if (loc!=null){
            String user_lng = String.valueOf(loc.getLongitude());
            String user_lat = String.valueOf(loc.getLatitude());
            String term = spnTermino.getSelectedItem().toString();
            //String gets = "?term="+term+"&limit=30&order_by=distance&radio=30&user_lat="+user_lat+"&user_lng="+user_lng;
            String uRL = Generalidades.urlApi;//+gets;
            Negocio.ejecutarSQL("DELETE FROM tbl_ultima_opcion",getApplicationContext());
            Negocio.ejecutarSQL("INSERT INTO tbl_ultima_opcion(ultima_opcion) VALUES('"+term+"')",getApplicationContext());
            Intent i = new Intent(this, ActividadGeneral.class);
            i.putExtra(Generalidades.user_lat,user_lat);
            i.putExtra(Generalidades.user_lng,user_lng);
            i.putExtra(Generalidades.term,term);
            i.putExtra(Generalidades.uRL,uRL);
            startActivity(i);
        }
    }
    private void inicioThreadGPS(){
        try {
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (habilitado == null) {
                        preguntar_GPS();
                        if(habilitado==null || habilitado==false){
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) { }
                        }
                    }
                    correr_original();
                }
            });
            tr.start();
        }catch (Exception er){ }
    }
    /*Método de conulta para el inicio de la plataforma*/
    private void preguntar_GPS() {
        LocationManager handle = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPSEnabled = handle.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = handle.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                int hasLocationPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                hasLocationPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                return;
            }else{
                habilitado = true;
            }
        }else{
            if(!isGPSEnabled && !isNetworkEnabled) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }else{
                habilitado = true;
            }
        }
    }
    /**Metodo de inicio del UI**/
    private void correr_original(){
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                miLocalizador = new Localizacion(getApplicationContext());
                iniciar_posicinonamiento();
            }
        });
    }
    /**Inicia posicionamiento del móvil*/
    private void iniciar_posicinonamiento() {
        miLocalizador.comenzarLocalizacion();
        iniciar();
    }
    /**Inicia recorrido de opciones de la ultima sesion del usuario y hablilita entrada de usuario*/
    void iniciar(){
        String sql = "SELECT ultima_opcion FROM tbl_ultima_opcion";
        String pais = Negocio.listarString(sql,this);
        if(!pais.equals("")) {
            String[] paises = getResources().getStringArray(R.array.terminos);
            for (int i = 0; i < paises.length; i++) {
                String p = paises[i];
                if (p.equals(pais)) {
                    spnTermino.setSelection(i);
                    break;
                }
            }
        }
        btnIngresar.setEnabled(true);
    }
    //todo=Método de respuesta de ActivitySystem de permiso solicitado
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    habilitado = true;
                }
            }
        }
    }

}
