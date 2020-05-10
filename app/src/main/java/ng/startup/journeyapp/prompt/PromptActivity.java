package ng.startup.journeyapp.prompt;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
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
import ng.startup.journeyapp.fragments.goals.GoalsClass;
import ng.startup.journeyapp.fragments.today.StepsClass;

public class PromptActivity extends AppCompatActivity implements CardStackListener {

    private DrawerLayout drawerLayout;

    private CardStackLayoutManager manager;
    private PromptAdapter adapter;
    private CardStackView cardStackView;

    private DBHelper dbHelper;

    Button letsGetIt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        dbHelper = new DBHelper(this);

        setupCardStackView();

        letsGetIt = (Button) findViewById(R.id.letsGetitButton);

        letsGetIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PromptActivity.this, HomeScreen.class);
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
            adapter.notifyItemChanged(manager.getTopPosition());
            //Log.d("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", Right ");
        }else if(direction.toString().equalsIgnoreCase("Left")){
            adapter.notifyItemChanged(manager.getTopPosition());
            //Log.d("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", Left ");
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

    private void initialize() {
        manager = new CardStackLayoutManager(getApplicationContext(), this);
        manager.setStackFrom(StackFrom.Right);
        manager.setVisibleCount(1);
        manager.setTranslationInterval(20.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        adapter = new PromptAdapter(this, createGoalsList());
        cardStackView = findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

    }

    private List<GoalsClass> createGoalsList() {

        List<GoalsClass> goalsList = new ArrayList<>();

        Cursor cursor = dbHelper.getGoals();

        //LOOP AND ADD TO ARRAYLIST
        while (cursor.moveToNext())
        {
            int goal_id = cursor.getInt(0);
            String goal_title = cursor.getString(1);
            String goal_description = cursor.getString(2);
            String goal_color = cursor.getString(3);
            String goal_due_date = cursor.getString(4);
            String goal_due_date_format = cursor.getString(5);
            String goal_date_created = cursor.getString(6);
            String goal_date_created_format = cursor.getString(7);
            String goal_status = cursor.getString(8);

            GoalsClass goalsClass = new GoalsClass(goal_id, goal_title, goal_description,
                    goal_color, goal_due_date, goal_due_date_format, goal_date_created,
                    goal_date_created_format, goal_status);

            //ADD TO ARRAYLIS
            goalsList.add(goalsClass);
        }

        if (goalsList.size() == 1){
            manager.setVisibleCount(1);
        }else if(goalsList.size() == 2){
            manager.setVisibleCount(2);
        }else{
            manager.setVisibleCount(3);
        }

        return goalsList;

    }

}
