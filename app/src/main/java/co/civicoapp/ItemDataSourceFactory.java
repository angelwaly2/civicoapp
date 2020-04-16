package co.civicoapp;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class ItemDataSourceFactory extends DataSource.Factory {

    private Double USER_LAT = Double.valueOf(0);
    private Double USER_LNG = Double.valueOf(0);
    private String TERM = "";

    public ItemDataSourceFactory(Double user_lat,Double user_lng,String term){
        this.USER_LAT = user_lat;
        this.USER_LNG = user_lng;
        this.TERM = term;
    }

    private MutableLiveData<PageKeyedDataSource<Integer,Result>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(USER_LAT,USER_LNG,TERM);
        itemLiveDataSource.postValue(itemDataSource);
        return  itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Result>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
