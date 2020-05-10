package ng.startup.journeyapp.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import ng.startup.journeyapp.R;
import ng.startup.journeyapp.authentication.LoginScreen;
import ng.startup.journeyapp.database.DBHelper;
import ng.startup.journeyapp.database.DBManager;

public class LandingScreen extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final String [] textStrings = {"Take a step everyday in the direction of your dreams", "", "", ""};
    private static final Integer[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};
    private ArrayList<Integer> imagesArray = new ArrayList<Integer>();
    private ArrayList<String> stringArray = new ArrayList<String>();

    Button letsGoButton;

    public static final String MyStoredPREFERENCES = "MyStoredPrefs" ;
    public static final String USERNAME = "username";
    public static final String IS_FIRST_TIME = "first_time_user";
    SharedPreferences sharedpreferences;

    private DBManager dbManager;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        init();

        letsGoButton = (Button) findViewById(R.id.letsGoButton);

        dbManager = new DBManager(this);
        dbManager.open();


        dbManager.insert_a_step("", "", "hdhdhd","",
                        "","","Discard");
        dbManager.insert_a_goal("", "", "", "",
                "", "", "", "Discard");

        letsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getSharedPreferences(MyStoredPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(IS_FIRST_TIME, "NO");
                editor.apply();

                Intent intent = new Intent(LandingScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        for (int i = 0, m = 0; i < images.length && m < textStrings.length; i++, m++){
            imagesArray.add(images[i]);
            stringArray.add(textStrings[m]);
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImageAdapter(LandingScreen.this, imagesArray, stringArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
    }
}
