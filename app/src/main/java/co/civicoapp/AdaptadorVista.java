package co.civicoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;


public class AdaptadorVista extends PagerAdapter {
    Context c;
    int total_paginas = 2;
    MapView mMapView;
    SupportMapFragment mapFragment;
    ActividadGeneral la;


    public AdaptadorVista(Context c,ActividadGeneral la){
        this.c = c;
        this.la = la;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout)object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((RelativeLayout)object);
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    @SuppressLint("InflateParams") public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        switch (position){
            case 0:
                v = inflater.inflate(R.layout.modelo_lugares,null);
                break;
            case 1:
                v = inflater.inflate(R.layout.modelo_lugares,null);
                break;
        }
        ((ViewPager)container).addView(v, 0);
        return v;
    }
    @Override
    public int getCount() {
        return total_paginas;
    }

}
