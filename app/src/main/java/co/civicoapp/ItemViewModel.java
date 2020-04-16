package co.civicoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.LivePagedListBuilder;

public class ItemViewModel extends ViewModel {

    public Double USER_LAT = Double.valueOf(0);
    public Double USER_LNG = Double.valueOf(0);
    public String TERM = "";

    LiveData<PagedList<Result>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer,Result>> liveDataSource;

    public ItemViewModel(Double user_lat,Double user_lng,String term){
        this.USER_LAT = user_lat;
        this.USER_LNG = user_lng;
        this.TERM = term;
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory(USER_LAT,USER_LNG,TERM);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config = (new PagedList.Config.Builder()).
                setEnablePlaceholders(false)
                .setPageSize(30)
                .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory,config)).build();
    }
}
