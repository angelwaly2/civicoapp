package co.civicoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadGeneral extends AppCompatActivity {
    /*****OBJECTS*****/
    Double esta_latitud;
    Double esta_longitud;
    String termino = null;
    String uRL = null;
    TextView txtCargando,txtinfo;
    VisorPaginas vp;
    Fragment enLugares,enOfertas;
    public GoogleMap map = null;
    List<Result> listaLugares;
    View promptsView = null;
    /*****************/
    /**todo=Método de creación*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_general);
        // Carga de objetos iniciales enviados de InicioActivity
        inicioVariablesObjectos();
        Negocio.mostarProgreso(true,this,vp,txtCargando); // Muestra en Proceso o carga Lugares
        //descargar_json_offers(1);
        descargar_json_places(1);
    }
    private void inicioVariablesObjectos(){
        Intent i = getIntent();
        Bundle b = i.getExtras();
        esta_latitud = Double.parseDouble(b.getString(Generalidades.user_lat));
        esta_longitud =Double.parseDouble(b.getString(Generalidades.user_lng));
        termino = b.getString(Generalidades.term);
        uRL = b.getString(Generalidades.uRL);
        txtCargando = (TextView) findViewById(R.id.txtCargando);
        txtinfo = (TextView) findViewById(R.id.txtinfo);
        vp = (VisorPaginas) findViewById(R.id.vistaPrincipal);
        carga_de_fragmens();
        Button btnLugares = (Button) findViewById(R.id.btnLugares);
        btnLugares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);
            }
        });
        Button btnOfertas = (Button) findViewById(R.id.btnOfertas);
        btnOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1);
            }
        });
    }

    public void actualizar_referencias(GoogleMap map){
        this.map = map;
        this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                click_on_marcker(marker);
                return false;
            }
        });
        cargar_lista_lugares_ofertas();
    }

    private void click_on_marcker(Marker marker){
        int position = (int) marker.getTag();
        Result result = listaLugares.get(position);
        if(result!=null){
            Negocio.mostrar_pop_up(result,this,promptsView);
        }
    }

    private void carga_de_fragmens(){
        enLugares = new EnLugares(esta_latitud,esta_longitud,this);
        enOfertas = new EnOfertas(esta_latitud,esta_longitud,termino,this);
        List<Fragment> tabs = new ArrayList<>();
        tabs.add(enLugares);
        tabs.add(enOfertas);
        PagerAdapter adaptador = new PagerAdapter(getSupportFragmentManager(), tabs);
        vp.setAdapter(adaptador);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> tabs;
        public PagerAdapter(FragmentManager frag, List<Fragment> tabs) {
            super(frag);
            this.tabs = tabs;
        }
        @Override
        public Fragment getItem(int position) {
            return tabs.get(position);
        }
        @Override
        public int getCount() {
            return tabs.size();
        }
    }

    private void descargar_json_places(int pagina){
        // Mostrar Lugares del resultado en VP 1
        Call<StackApiResponse> call = RetrofitClient.getInstance().getApi().getAnswers(pagina,termino,esta_latitud,esta_longitud,30,"distance",30,"place");
        call.enqueue(new Callback<StackApiResponse>() {
            @Override
            public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {
                StackApiResponse stackApiResponse = response.body();
                List<Result> lista = stackApiResponse.results;
                termino_segundo_proceso(lista,1);
            }
            @Override
            public void onFailure(Call<StackApiResponse> call, Throwable t) {
                termino_segundo_proceso(null,1);
            }
        });
    }

    private void descargar_json_offers(int pagina){
        // Mostrar Lugares del resultado en VP 1
        Call<StackApiResponse> call = RetrofitClient.getInstance().getApi().getAnswers(pagina,termino,esta_latitud,esta_longitud,30,"distance",30,"offer");
        call.enqueue(new Callback<StackApiResponse>() {
            @Override
            public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {
                StackApiResponse stackApiResponse = response.body();
                List<Result> lista = stackApiResponse.results;
                termino_segundo_proceso(lista,2);
            }
            @Override
            public void onFailure(Call<StackApiResponse> call, Throwable t) {
                termino_segundo_proceso(null,2);
            }
        });
    }
    private void termino_segundo_proceso(List<Result> lista,int opcion){
        if(opcion==1) {
            Negocio.mostarProgreso(false, this, vp, txtCargando); // Termina Proceso de carga
            listaLugares = lista;
        }else if(opcion ==2){

        }
    }
    private void cargar_lista_lugares_ofertas(){
        if(listaLugares!=null){
            for(int conta=0;conta<listaLugares.size();conta++){
                // Put any markers of places
                Result result = listaLugares.get(conta);
                if(result.type.equals("Lugar")) {
                    MarkerOptions mo = new MarkerOptions();
                    LatLng pos = new LatLng(result.coordinates.lat, result.coordinates.long1);
                    mo.position(pos);
                    mo.title(result.name);
                    Marker m = map.addMarker(mo);
                    m.setTag(conta);
                }
            }
        }else {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
}
