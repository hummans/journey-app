package ng.startup.journeyapp.fragments.today;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import ng.startup.journeyapp.fragments.goals.AddGoal;

public class AddStepGoalSelect extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    Button addStep, cancelStep;
    EditText stepTitle;
    String goalTitle, dateCreated, dateCreatedFormat, stepTitleString, goalColor;

    private DBManager dbManager;
    DBHelper db;

    Spinner spinnerGoals;

    List<String> lables;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step_goal_select);
        //setTitle("Add Step");
        //getSupportActionBar().setElevation(0);

        dbManager = new DBManager(this);
        dbManager.open();

        spinnerGoals = (Spinner) findViewById(R.id.spinnerGoals);
        stepTitle = (EditText) findViewById(R.id.editTextStepTitle);
        cancelStep = (Button) findViewById(R.id.cancelStepButton);
        addStep = (Button) findViewById(R.id.addStepButton);

        // Spinner click listener
        spinnerGoals.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        cancelStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStepGoalSelect.this.finish();
            }
        });

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insert_step();
                Intent intent = new Intent(AddStepGoalSelect.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void loadSpinnerData() {
        // database handler
        db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        lables = db.getAllLabels();
        lables.add("+ ADD GOAL");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGoals.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        goalTitle = parent.getItemAtPosition(position).toString();

        if(goalTitle.equalsIgnoreCase("+ Add Goal")){
            spinnerGoals.setSelection(0);
            dataAdapter.notifyDataSetChanged();
            Intent intent = new Intent(AddStepGoalSelect.this, AddGoal.class);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void insert_step (){

        db = new DBHelper(getApplicationContext());

        Date c = Calendar.getInstance().getTime();
        String myFormat = "EEEE, dd MMMM, yyyy";
        SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);
        dateCreated = df.format(c);

        String myFormat2 = "dd,MM,yyyy,MMM";
        SimpleDateFormat df2 = new SimpleDateFormat(myFormat2, Locale.US);
        dateCreatedFormat = df2.format(c);

        goalColor = db.getColor(goalTitle);

        stepTitleString = stepTitle.getText().toString();

        dbManager.insert_a_step(stepTitleString, dateCreated, dateCreatedFormat,
                goalTitle, goalColor,"","Pending");

        dbManager.insert_a_streak(dateCreatedFormat, 0);

    }

}