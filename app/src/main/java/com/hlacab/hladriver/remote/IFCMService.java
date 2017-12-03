package com.hlacab.hladriver.remote;

import com.hlacab.hladriver.model.FCMResponse;
import com.hlacab.hladriver.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by gopinath on 03/12/17.
 */

public interface IFCMService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAByFW-9A:APA91bGKSohUJQMeCovPo4nwMau8go7CB7ohu19M2ZcABvHwn8UCSNWEG6W45Z-Wizr-W9ckBgG-rsaDLB6GSoyRkrsqzg1zqNxQF6T8FinRH5VBn41TMNM5ToJbYC9bnqrIqFUhNAei"
    })

    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
