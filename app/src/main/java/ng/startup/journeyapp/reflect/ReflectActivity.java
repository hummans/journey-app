package ng.startup.journeyapp.reflect;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ng.startup.journeyapp.HomeScreen;
import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBHelper;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.fragments.goals.GoalsClass;
import ng.startup.journeyapp.fragments.today.StepsClass;
import ng.startup.journeyapp.prompt.PromptActivity;
import ng.startup.journeyapp.prompt.PromptAdapter;

public class ReflectActivity extends AppCompatActivity implements CardStackListener {

    private DrawerLayout drawerLayout;

    private CardStackLayoutManager manager;
    private ReflectAdapter adapter;
    private CardStackView cardStackView;

    private DBHelper dbHelper;
    private DBManager dbManager;

    Button finishButton;
    List<StepsClass> stepsList;

    String stepTitle;

    ImageView swipeLeft, swipeRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);

        stepsList = new ArrayList<>();

        dbHelper = new DBHelper(this);
        dbManager = new DBManager(this);
        dbManager.open();

        setupCardStackView();
        //setupButton();

        finishButton = (Button) findViewById(R.id.finishButton);
        swipeLeft = (ImageView) findViewById(R.id.swipe_left);
        swipeRight = (ImageView) findViewById(R.id.swipe_right);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(5000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        swipeLeft.startAnimation(anim);
        swipeRight.startAnimation(anim);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReflectActivity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

        Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);

    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", d = " + direction);

        if(direction.toString().equalsIgnoreCase("Right")){

            int finalPosition = manager.getTopPosition() - 1;
            stepTitle = stepsList.get(finalPosition).getStepTitle();

            dbManager.update_status_in_steps(stepTitle, "Pending");
            adapter.notifyItemChanged(manager.getTopPosition());
            //Log.d("CardStackView", "onCardSwiped: p = " + stepTitle + ", d = " + direction);

        }else if(direction.toString().equalsIgnoreCase("Left")){

            int finalPosition = manager.getTopPosition() - 1;
            stepTitle = stepsList.get(finalPosition).getStepTitle();

            dbManager.update_status_in_steps(stepTitle, "Done");
            adapter.notifyItemChanged(manager.getTopPosition());
            //Log.d("CardStackView", "onCardSwiped: p = " + stepTitle + ", d = " + direction);

        }

    }

    @Override
    public void onCardRewound() {
        Log.d("CardStackView", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled:" + manager.getTopPosition());
    }

    private void setupCardStackView() {
        initialize();
    }

    /*private void setupButton() {
        View skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });

        View rewind = findViewById(R.id.rewind_button);
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setRewindAnimationSetting(setting);
                cardStackView.rewind();
            }
        });

        View like = findViewById(R.id.like_button);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
    }*/

    private void initialize() {
        manager = new CardStackLayoutManager(getApplicationContext(), this);
        manager.setStackFrom(StackFrom.Right);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(20.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        adapter = new ReflectAdapter(this, createStepsList());
        cardStackView = findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private List<StepsClass> createStepsList() {

        Date c = Calendar.getInstance().getTime();
        String myFormat2 = "dd,MM,yyyy,MMM";
        SimpleDateFormat df2 = new SimpleDateFormat(myFormat2, Locale.US);
        String dateCreatedFormat = df2.format(c);


        Cursor cursor = dbHelper.getStepsOnDate(dateCreatedFormat);

        //LOOP AND ADD TO ARRAYLIST
        while (cursor.moveToNext())
        {
            int step_id = cursor.getInt(0);
            String step_title = cursor.getString(1);
            String step_date_created = cursor.getString(2);
            String step_date_created_format = cursor.getString(3);
            String goal_title = cursor.getString(4);
            String goal_color = cursor.getString(5);
            String step_journal = cursor.getString(6);
            String step_status = cursor.getString(7);

            StepsClass stepsClass = new StepsClass(step_id, step_title, step_date_created,
                    step_date_created_format, goal_color, goal_title, step_journal, step_status);

            //ADD TO ARRAYLIS
            stepsList.add(stepsClass);
        }

        return stepsList;

    }
}
