package co.civicoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("query")
    Call<StackApiResponse> getAnswers(
            @Query("page") int page,
            @Query("term") String term,
            @Query("user_lat") double user_lat,
            @Query("user_lng") double user_lng,
            @Query("limit") int limit,
            @Query("order_by") String order_by,
            @Query("radio") int radio,
            @Query("entities") String entities

    );
}
