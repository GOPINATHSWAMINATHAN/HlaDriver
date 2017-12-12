package com.hlacab.hladriver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hlacab.hladriver.common.Common;
import com.hlacab.hladriver.model.User;

public class Profile extends AppCompatActivity {
    EditText username, password, phoneno, name;
    Button login;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_profile);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {

        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
            dialog.setMessage("Please switch on your location service. It has disabled!");
            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getApplicationContext().startActivity(myIntent);

                }
            });

            dialog.setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "We could not track you, if the location disabled!", Toast.LENGTH_LONG).show();
                }
            });


        }
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.user_driver_tb1);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        phoneno = (EditText) findViewById(R.id.phoneno);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String user = username.getText().toString();
                final String pass = password.getText().toString();

                auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(Profile.this
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "SignUp Failed!" + task, Toast.LENGTH_LONG).show();
                                } else {

                                    FirebaseDatabase.getInstance().getReference(Common.user_driver_tb1)
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Common.currentUser = dataSnapshot.getValue(User.class);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                    User user = new User();
                                    user.setName(name.getText().toString());
                                    user.setEmail(username.getText().toString());
                                    user.setPhoneno(phoneno.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });


            }
        });


    }
}
