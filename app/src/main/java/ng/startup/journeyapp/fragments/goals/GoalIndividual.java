package ng.startup.journeyapp.fragments.goals;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBHelper;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.fragments.today.StepIndividual;
import ng.startup.journeyapp.fragments.today.StepsClass;
import ng.startup.journeyapp.reflect.ReflectActivity;

public class GoalIndividual extends AppCompatActivity {

    String goalTitle, goalDueDate, goalDueDateFormat, goalDateCreated, getGoalDateCreatedFormat;
    String goalDescription, goalColor, goalDaysLeft, goalStatus;
    int goalID;

    TextView goalTitleText, goalNoOfDays;

    RelativeLayout top_bar;

    CheckBox checkCalendar;
    private DBHelper dbHelper;
    private DBManager dbManager;

    RecyclerView recyclerView;
    GoalStepsAdapter adapter;
    ArrayList<StepsClass> stepsList = new ArrayList<>();
    List<String> labelsDone, labelsPending;

    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 3000;

    String dateDone, datePending, setDate;
    Calendar calendarDone, calendarPending;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_individual);

        getSupportActionBar().setElevation(0);

        dbManager = new DBManager(this);
        dbManager.open();

        List<EventDay> events = new ArrayList<>();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        top_bar = (RelativeLayout) findViewById(R.id.top_bar);
        goalTitleText = (TextView) findViewById(R.id.goalTitle);
        goalNoOfDays = (TextView) findViewById(R.id.goalNoOfDays);
        checkCalendar = (CheckBox) findViewById(R.id.checkBoxCalendar);

        dbHelper = new DBHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Bundle extras = getIntent().getExtras();
        goalID = extras.getInt("goalId");
        goalTitle = extras.getString("goalTitle");
        goalDescription = extras.getString("goalDescription");
        goalColor = extras.getString("goalColor");
        goalDueDate = extras.getString("goalDueDate");
        goalDueDateFormat = extras.getString("goalDueDateFormat");
        goalDateCreated = extras.getString("goalDateCreated");
        getGoalDateCreatedFormat = extras.getString("getGoalDateCreatedFormat");
        goalDaysLeft = extras.getString("goalDaysLeft");
        goalStatus = extras.getString("goalStatus");

        adapter=new GoalStepsAdapter(this, stepsList);

        goalTitleText.setText(goalTitle);
        goalNoOfDays.setText(goalDaysLeft);

        labelsPending = dbHelper.getDatesPending(goalTitle);

        for (int i = 0; i < labelsPending.size(); i++) {
            datePending = labelsPending.get(i).toString();
            String partsPending[] = datePending.split(",");

            int dayPending = Integer.parseInt(partsPending[0]);
            int monthPending = Integer.parseInt(partsPending[1]);
            int yearPending = Integer.parseInt(partsPending[2]);

            calendarPending = DateUtils.getCalendar();
            calendarPending.set(Calendar.YEAR, yearPending);
            calendarPending.set(Calendar.MONTH, (monthPending - 1));
            calendarPending.set(Calendar.DAY_OF_MONTH, dayPending);

            try {
                calendarView.setDate(Calendar.getInstance().getTime());
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }

            events.add(new EventDay(calendarPending, R.drawable.button_curved_edges_white));

        }

        labelsDone = dbHelper.getDatesDone(goalTitle);

        for (int i = 0; i < labelsDone.size(); i++) {
            dateDone = labelsDone.get(i).toString();
            String partsDone[] = dateDone.split(",");

            int dayDone = Integer.parseInt(partsDone[0]);
            int monthDone = Integer.parseInt(partsDone[1]);
            int yearDone = Integer.parseInt(partsDone[2]);

            calendarDone = DateUtils.getCalendar();
            calendarDone.set(Calendar.YEAR, yearDone);
            calendarDone.set(Calendar.MONTH, (monthDone - 1));
            calendarDone.set(Calendar.DAY_OF_MONTH, dayDone);

            try {
                calendarView.setDate(Calendar.getInstance().getTime());
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }

            events.add(new EventDay(calendarDone, R.drawable.round_grey));

        }

        calendarView.setEvents(events);

        calendarView.setOnDayClickListener((EventDay eventDay) -> {
            String myFormat = "dd,MM,yyyy,MMM";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            setDate = sdf.format(eventDay.getCalendar().getTime());

            stepsList.clear();
            init(recyclerView, setDate);

        });

        checkCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calendarView.setVisibility(View.VISIBLE);
                    stepsList.clear();
                    init(recyclerView, setDate);
                }
                if (!isChecked) {
                    calendarView.setVisibility(View.GONE);
                    stepsList.clear();
                    retrieveStepsFromGoal();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.goal_individual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            return true;
        }
        if (id == R.id.action_edit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(RecyclerView recyclerView, String on_date) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        retrieveSteps(on_date);
        final GoalStepsAdapter adapter = new GoalStepsAdapter(this, stepsList);

        //CHECK IF ARRAYLIST ISNT EMPTY
        if(!(stepsList.size()<1))
        {
            recyclerView.setAdapter(adapter);
        }

        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(recyclerView),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onPendingDismiss(RecyclerViewAdapter recyclerView, int position) {

                            }

                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {
                                dbManager.update_status_in_steps(stepsList.get(position).getStepTitle(), "Done");
                                stepsList.clear();
                                retrieveSteps(on_date);
                                adapter.notifyDataSetChanged();
                            }
                        });
        touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
        recyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        recyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(this,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_delete) {
                            touchListener.processPendingDismisses();
                        } else if (view.getId() == R.id.txt_undo) {
                            touchListener.undoPendingDismiss();
                        } else { // R.id.txt_data
                            Intent intent = new Intent(GoalIndividual.this, StepIndividual.class);
                            intent.putExtra("stepId",stepsList.get(position).getStepId());
                            intent.putExtra("stepTitle",stepsList.get(position).getStepTitle());
                            intent.putExtra("stepDateCreated",stepsList.get(position).getStepDateCreated());
                            intent.putExtra("stepDateCreatedFormat",stepsList.get(position).getStepDateCreatedFormat());
                            intent.putExtra("goalColor",stepsList.get(position).getGoalColor());
                            intent.putExtra("goalTitle",stepsList.get(position).getGoalTitle());
                            intent.putExtra("stepJournal",stepsList.get(position).getStepJournal());
                            intent.putExtra("stepStatus",stepsList.get(position).getStepStatus());
                            startActivity(intent);
                        }
                    }
                }));
    }

    private void retrieveSteps(String on_date) {

        Cursor cursor = dbHelper.getStepsOnDate(on_date, goalTitle);

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

        //CHECK IF ARRAYLIST ISNT EMPTY
        if(!(stepsList.size()<1))
        {
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(), "No Steps for this day", Toast.LENGTH_SHORT).show();
        }

    }

    private void retrieveStepsFromGoal() {

        Cursor cursor = dbHelper.getStepsFromGoal(goalTitle);

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

        //CHECK IF ARRAYLIST ISNT EMPTY
        if(!(stepsList.size()<1))
        {
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(getApplicationContext(), "No Steps added for this goal", Toast.LENGTH_SHORT).show();
        }

    }

}
