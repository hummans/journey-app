package ng.startup.journeyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ng.startup.journeyapp.database.DBHelper;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.onboarding.LandingScreen;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;

    private DBManager dbManager;
    DBHelper db;

    String dateCreated;

    public static final String MyStoredPREFERENCES = "MyStoredPrefs" ;
    public static final String USERNAME = "username";
    public static final String IS_FIRST_TIME = "first_time_user";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbManager = new DBManager(this);
        dbManager.open();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedpreferences = getSharedPreferences(MyStoredPREFERENCES, Context.MODE_PRIVATE);
                String storedFirstTime = sharedpreferences.getString(IS_FIRST_TIME, "");

                if (storedFirstTime.equalsIgnoreCase("NO")){

                    Intent intent = new Intent(SplashScreen.this, HomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }else{

                    Intent intent = new Intent(SplashScreen.this, LandingScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();

                }




            }
        }, SPLASH_DISPLAY_LENGTH);

    }


}
