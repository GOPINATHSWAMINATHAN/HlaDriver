package com.hlacab.hladriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Profile extends AppCompatActivity {
EditText username,password;
Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        String user=username.getText().toString();
        String pass=password.getText().toString();

        Intent i=new Intent(this,MapsActivity.class);
        Bundle b=new Bundle();
        b.putString("user",user);
        b.putString("pass",pass);
        i.putExtras(b);
        startActivity(i);


    }
}
