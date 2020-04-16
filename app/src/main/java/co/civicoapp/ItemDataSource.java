package co.civicoapp;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, Result> {

    private static final int FIRST_PAGE = 1;
    private String TERM = "";
    private Double USER_LAT = Double.valueOf(0);
    private Double USER_LNG = Double.valueOf(0);
    private static final int LIMIT = 30;
    private static final String ORDER_BY = "distance";
    private static final int RADIO = 30;
    private static final String ENTITIES = "offer";


    public ItemDataSource(Double user_lat,Double user_lng,String term){
        this.USER_LAT = user_lat;
        this.USER_LNG = user_lng;
        this.TERM = term;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Result> callback) {
        RetrofitClient.getInstance().getApi()
                .getAnswers(FIRST_PAGE,TERM,USER_LAT,USER_LNG,LIMIT,ORDER_BY,RADIO,ENTITIES)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {

                        if(response.body()!=null){
                            callback.onResult(response.body().results,null,FIRST_PAGE+1);
                        }
                    }
                    @Override
                    public void onFailure(Call<StackApiResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {
        RetrofitClient.getInstance().getApi().getAnswers(params.key,TERM,USER_LAT,USER_LNG,LIMIT,ORDER_BY,RADIO,ENTITIES)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {
                        Integer key = (params.key > 1) ? params.key -1 : null;
                        if(response.body()!=null){
                            callback.onResult(response.body().results,key);
                        }
                    }

                    @Override
                    public void onFailure(Call<StackApiResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {
        RetrofitClient.getInstance().getApi().getAnswers(params.key,TERM,USER_LAT,USER_LNG,LIMIT,ORDER_BY,RADIO,ENTITIES)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {

                        Integer key = (response.body().total_pages == response.body().current_page) ? params.key +1 : null;
                        if(response.body()!=null){
                            callback.onResult(response.body().results,key);
                        }
                    }

                    @Override
                    public void onFailure(Call<StackApiResponse> call, Throwable t) {

                    }
                });
    }
}
