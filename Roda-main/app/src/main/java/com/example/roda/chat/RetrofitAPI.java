package com.example.roda.chat;


import com.example.roda.model.botModels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<botModels> getMessage(@Url String url);

    @GET
    Call<ResponseBody> getStringResponse(@Url String url);

}
