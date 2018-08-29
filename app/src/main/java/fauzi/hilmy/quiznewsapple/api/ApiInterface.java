package fauzi.hilmy.quiznewsapple.api;

import fauzi.hilmy.quiznewsapple.response.ResponseNews;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("everything?q=apple&apiKey=795f5a13a92a46b1bfdd25f8d1307c23")
    Call<ResponseNews> readJadwalShalat();
}
