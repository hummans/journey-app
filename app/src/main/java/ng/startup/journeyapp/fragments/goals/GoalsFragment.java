package ng.startup.journeyapp.fragments.goals;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBHelper;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.fragments.today.StepsAdapter;
import ng.startup.journeyapp.fragments.today.StepsClass;

public class GoalsFragment extends Fragment {

    Button addGoal;
    FloatingActionButton fabAddGoal;
    Calendar myCalendar = Calendar.getInstance();

    String daysLeft;

    private DBManager dbManager;
    DBHelper db;

    RecyclerView recyclerView2;
    GoalsAdapter adapter;
    ArrayList<GoalsClass> goalsList = new ArrayList<>();

    static int y;

    public static GoalsFragment newInstance() {
        GoalsFragment fragment = new GoalsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_goals, null);

        //recycler
        recyclerView2 = (RecyclerView) x.findViewById(R.id.recyclerview);
        fabAddGoal = (FloatingActionButton) x.findViewById(R.id.fabAddGoal);

        //SET PROPS
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                y=dy;

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(recyclerView2.SCROLL_STATE_DRAGGING==newState){
                    //addStep.setVisibility(View.GONE);
                }
                if(recyclerView2.SCROLL_STATE_IDLE==newState){
                    // fragProductLl.setVisibility(View.VISIBLE);
                    if(y<=0){
                        //addStep.startAnimation(slide_up);
                        addGoal.setVisibility(View.VISIBLE);
                        //fabAddStep.startAnimation(slide_down);
                        fabAddGoal.setVisibility(View.GONE);
                    }
                    else{
                        y=0;

                        addGoal.setVisibility(View.GONE);
                        fabAddGoal.setVisibility(View.VISIBLE);


                    }
                }
            }


        });

        //ADAPTER
        adapter=new GoalsAdapter(getActivity(), goalsList);

        //RETRIEVE
        retrieve();

        addGoal = (Button) x.findViewById(R.id.addGoal);

        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGoal.class);
                startActivity(intent);
            }
        });
        fabAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGoal.class);
                startActivity(intent);
            }
        });


        return x;
    }

    private void retrieve() {

        dbManager = new DBManager(getActivity());
        dbManager.open();

        db = new DBHelper(getActivity());

        Cursor cursor = dbManager.fetch_all_goals();

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

            String noOfSteps = db.getNoStepsforGoal(goal_title);

            try {

                Date c = Calendar.getInstance().getTime();
                String newFormat2 = "MM/dd/yyyy";
                SimpleDateFormat df = new SimpleDateFormat(newFormat2, Locale.US);
                String todayDate = df.format(c);

                Date date1;
                Date date2;

                SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

                date1 = dates.parse(todayDate);
                date2 = dates.parse(goal_due_date_format);

                //Comparing dates
                long difference = date2.getTime() - date1.getTime() ;
                if(difference < 0){
                    daysLeft = "0";
                }else if(difference > 0){
                    long differenceDates = difference / (24 * 60 * 60 * 1000);
                    daysLeft = Long.toString(differenceDates);
                }

            }
            catch (Exception exception) {
                Log.e("DIDN'T WORK", "exception " + exception);
            }

            GoalsClass goalsClass = new GoalsClass(goal_id, goal_title, goal_description,
                    goal_color, goal_due_date, goal_due_date_format, goal_date_created,
                    goal_date_created_format, daysLeft, noOfSteps, goal_status);

            //ADD TO ARRAYLIS
            goalsList.add(goalsClass);
        }

        //CHECK IF ARRAYLIST ISNT EMPTY
        if(!(goalsList.size()<1))
        {
            recyclerView2.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        dbManager.closeDB();

    }

}