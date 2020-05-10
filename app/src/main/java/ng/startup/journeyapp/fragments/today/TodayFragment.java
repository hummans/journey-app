package ng.startup.journeyapp.fragments.today;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import ng.startup.journeyapp.fragments.goals.GoalsAdapter;
import ng.startup.journeyapp.prompt.PromptActivity;
import ng.startup.journeyapp.reflect.ReflectActivity;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by mungwaagu on 05/11/2018.
 */

public class TodayFragment extends Fragment {

    TextView weekDay, date, totalNoSteps, totalNoGoals, noStepsAdded, dayStreak;
    Button addStep;
    FloatingActionButton fabAddStep;
    Calendar myCalendar = Calendar.getInstance();

    String totalSteps, totalGoals;

    private DBManager dbManager;
    private DBHelper dbHelper;
    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 3000;

    RecyclerView rv;
    ArrayList<StepsClass> stepsList = new ArrayList<>();
    Animation slide_up, slide_down, anim_slide_out;

    List<String> labelsStreakDates;
    String streakDate;

    static int y;

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_today, null);

        totalNoGoals = (TextView) x.findViewById(R.id.totalNoOfGoals);
        totalNoSteps = (TextView) x.findViewById(R.id.totalNoOfSteps);
        noStepsAdded = (TextView) x.findViewById(R.id.textNoSTeps);
        dayStreak = (TextView) x.findViewById(R.id.dayStreak);


        dbManager = new DBManager(getActivity());
        dbManager.open();
        dbHelper = new DBHelper(getActivity());

        labelsStreakDates = dbHelper.getStreakDates();
        for (int i = 0; i < labelsStreakDates.size(); i++) {

            List<String> dateStatuses;

            streakDate = labelsStreakDates.get(i).toString();

            dateStatuses = dbHelper.getStreakDateStatus(streakDate);
            if (dateStatuses.size() < 1){
               dbManager.update_value_streak(streakDate, 1);
            }else {
                dbManager.update_value_streak(streakDate, 0);
            }

        }

        String noOfStreaks = dbHelper.getStreaks();
        dayStreak.setText(noOfStreaks);

        totalSteps = dbHelper.getStatsSteps();
        totalGoals = dbHelper.getStatsGoals();

        totalNoSteps.setText(totalSteps);
        totalNoGoals.setText(totalGoals);

        rv = (RecyclerView) x.findViewById(R.id.recyclerview);
        addStep = (Button) x.findViewById(R.id.addStep);
        fabAddStep = (FloatingActionButton) x.findViewById(R.id.fabAddStep);

        init(rv);


        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        anim_slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_out_right);

        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                y=dy;
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(rv.SCROLL_STATE_DRAGGING==newState){
                    addStep.setVisibility(View.GONE);
                }
                if(rv.SCROLL_STATE_IDLE==newState){
                    // fragProductLl.setVisibility(View.VISIBLE);
                    if(y<=0){
                        //addStep.startAnimation(slide_up);
                        addStep.setVisibility(View.VISIBLE);
                        //fabAddStep.startAnimation(slide_down);
                        fabAddStep.setVisibility(View.GONE);
                    }
                    else{
                        y=0;

                        addStep.setVisibility(View.GONE);
                        fabAddStep.setVisibility(View.VISIBLE);


                    }
                }
            }


        });


        String myFormat = "EEEE,"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String currentTime = sdf.format(myCalendar.getTime());

        String myFormat2 = "dd MMMM, yyyy"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
        String currentTime2 = sdf2.format(myCalendar.getTime());

        weekDay = (TextView) x.findViewById(R.id.weekDay);
        date = (TextView) x.findViewById(R.id.date);


        weekDay.setText(currentTime);
        date.setText(currentTime2);

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddStepGoalSelect.class);
                startActivity(intent);
                //getActivity().finish();

            }
        });

        fabAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddStepGoalSelect.class);
                startActivity(intent);
            }
        });


        return x;
    }

    private void init(RecyclerView recyclerView) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        retrieve();
        final StepsAdapter adapter = new StepsAdapter(getActivity(), stepsList);

        //CHECK IF ARRAYLIST ISNT EMPTY
        if(!(stepsList.size()<1))
        {
            noStepsAdded.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(adapter);
        }else{
            noStepsAdded.setVisibility(View.VISIBLE);
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
                                retrieve();
                                adapter.notifyDataSetChanged();

                            }
                        });
        touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
        recyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        recyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(getActivity(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_delete) {
                            touchListener.processPendingDismisses();
                        } else if (view.getId() == R.id.txt_undo) {
                            touchListener.undoPendingDismiss();
                        } else { // R.id.txt_data
                            Intent intent = new Intent(getActivity(), StepIndividual.class);
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

    //RETREIEV
    private void retrieve() {

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

        //dbManager.closeDB();

    }

}
