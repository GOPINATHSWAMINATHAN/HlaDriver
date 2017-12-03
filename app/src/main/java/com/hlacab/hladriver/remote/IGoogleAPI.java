package com.hlacab.hladriver.remote;

import retrofit2.Call;
import retrofit2.http.Url;

/**
 * Created by gopinath on 30/11/17.
 */

public interface IGoogleAPI {
    Call<String> getPath(@Url String url);
}
