package com.hlacab.hladriver.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by gopinath on 30/11/17.
 */

public interface IGoogleAPI {
    @GET
    Call<String> getPath(@Url String url);
}
