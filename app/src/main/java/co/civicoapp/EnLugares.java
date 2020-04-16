package co.civicoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class EnLugares extends Fragment implements OnMapReadyCallback {

    GoogleMap map = null;
    Double esta_latitud;
    Double esta_longitud;
    ActividadGeneral la;

    public EnLugares(Double esta_latitud,Double esta_longitud,ActividadGeneral la){
        this.esta_latitud = esta_latitud;
        this.esta_longitud = esta_longitud;
        this.la = la;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.modelo_lugares,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng namsan = new LatLng(esta_latitud, esta_longitud);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(namsan,16));
        la.actualizar_referencias(map);
    }
}
