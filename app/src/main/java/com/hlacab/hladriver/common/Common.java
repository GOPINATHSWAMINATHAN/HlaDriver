package com.hlacab.hladriver.common;

import android.location.Location;

import com.hlacab.hladriver.model.User;
import com.hlacab.hladriver.remote.FCMClient;
import com.hlacab.hladriver.remote.IFCMService;
import com.hlacab.hladriver.remote.IGoogleAPI;
import com.hlacab.hladriver.remote.RetrofitClient;

/**
 * Created by gopinath on 30/11/17.
 */

public class Common {

    public static final String driver_tb1 = "Drivers";
    public static final String user_driver_tb1 = "DriversInformation";
    public static final String user_rider_tb1 = "RidersInformation";
    public static final String pickup_request_tb1 = "PickupRequest";
    public static Location mLastLocation = null;
    public static final String token_tb1 = "Tokens";


    public static double base_fare=5.00;

    private static double time_rate=1.35;
    private static double distance_rate=2.50;

    public static double formulaPrice(double km,double min)
    {
        return base_fare+(distance_rate*km)+(time_rate*min);
    }


    public static User currentUser;

    public static final String baseURL = "https://maps.googleapis.com";

    public static final String fcmURL = "https://fcm.googleapis.com/";

    public static IGoogleAPI getGoogleAPI() {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static IFCMService getFCMService() {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

}
