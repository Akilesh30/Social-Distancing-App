package org.altbeacon.beaconreference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy" );
        String day=dateFormat.format(new Date());
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String date = sharedPreferences.getString("yesterday_date","");
        if(date.matches("")){editor.putString("yesterday_date",day);}

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,
                        IntroActivity.class);
                startActivity(i);
                finish();
            }
        }, 2500);
    }
}