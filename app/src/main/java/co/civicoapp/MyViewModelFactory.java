package co.civicoapp;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Double USER_LAT;
    private Double USER_LNG;
    private String TERM;


    public MyViewModelFactory(Double user_lat,Double user_lng,String term) {
        this.USER_LAT = user_lat;
        this.USER_LNG = user_lng;
        this.TERM = term;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ItemViewModel(USER_LAT,USER_LNG,TERM);
    }
}
