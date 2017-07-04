package omer.parking.com.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import omer.parking.com.R;
import omer.parking.com.service.GPSTracker;
import omer.parking.com.util.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GPSTracker gps;
        gps = new GPSTracker(this);

        gps.canGetLocation();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(SharedPrefManager.getInstance(SplashActivity.this).getFirstRun()) {
                    Intent intent = new Intent(SplashActivity.this, SettingsActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, OfficeInfoActivity.class);

                    startActivity(intent);
                    finish();
                }
            }
        };

        handler.postDelayed(runnable, 2000);
    }
}
