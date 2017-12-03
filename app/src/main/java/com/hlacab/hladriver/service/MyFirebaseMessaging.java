package com.hlacab.hladriver.service;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.hlacab.hladriver.ConsumerCall;

/**
 * Created by gopinath on 03/12/17.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService{

    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        LatLng customer_location=new Gson().fromJson(remoteMessage.getNotification().getBody(),LatLng.class);

        Intent intent=new Intent(getBaseContext(), ConsumerCall.class);
        intent.putExtra("lat",customer_location.latitude);
        intent.putExtra("lng",customer_location.longitude);
        startActivity(intent);

    }


}
