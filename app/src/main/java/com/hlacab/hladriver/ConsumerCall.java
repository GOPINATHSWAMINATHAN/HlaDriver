package com.hlacab.hladriver;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.hlacab.hladriver.common.Common;
import com.hlacab.hladriver.model.FCMResponse;
import com.hlacab.hladriver.model.Notification;
import com.hlacab.hladriver.model.Sender;
import com.hlacab.hladriver.model.Token;
import com.hlacab.hladriver.remote.IFCMService;
import com.hlacab.hladriver.remote.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsumerCall extends AppCompatActivity {

    TextView txtTime, txtAddress, txtDistance, timer;
    MediaPlayer mediaPlayer;
    Button btnCancel, btnAccept;
    IGoogleAPI mService;
    IFCMService mFCMService;
    String customerId;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_call);
        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtTime = (TextView) findViewById(R.id.txtTime);
        timer = (TextView) findViewById(R.id.timer);


        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnCancel = (Button) findViewById(R.id.btnDecline);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.shooting);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining:" + millisUntilFinished / 1000);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(customerId)) {
                            cancelBooking(customerId);
                        }
                    }
                });

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DriverTracking.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);

                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                        finish();
                    }
                });


                if (getIntent() != null) {
                    lat = getIntent().getDoubleExtra("lat", -1.0);
                    lng = getIntent().getDoubleExtra("lng", -1.0);
                    customerId = getIntent().getStringExtra("customer");
                    getDirection(lat, lng);
                }
            }

            @Override
            public void onFinish() {
                timer.setText("TimeOut!");
                finish();

            }
        }.start();

    }

    private void cancelBooking(String customerId) {
        Token token = new Token(customerId);
        Notification notification = new Notification("Cancel", "Driver has cancelled your request");
        Sender sender = new Sender(token.getToken(), notification);
        mFCMService.sendMessage(sender).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body().success == 1) {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });

    }

    private void getDirection(double lat, double lng) {


        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" + "transit_routing_preference=less_driving&" + "origin=" + Common.mLastLocation.getLatitude() + "," + Common.mLastLocation.getLongitude() + "&" + "destination=" + lat + "," + lng + "&" + "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("EDMTDEV", requestApi);

            mService.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsObject = legs.getJSONObject(0);
                        JSONObject distance = legsObject.getJSONObject("distance");
                        txtDistance.setText(distance.getString("text"));
                        JSONObject time = legsObject.getJSONObject("duration");
                        txtTime.setText(distance.getString("text"));

                        String address = legsObject.getString("end_address");
                        txtAddress.setText(address);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }
}
