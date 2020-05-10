package ng.startup.journeyapp.fragments.goals;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ng.startup.journeyapp.HomeScreen;
import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.fragments.today.AddStepGoalSelect;

public class AddGoal extends AppCompatActivity {

    RadioGroup radioGroup;
    String colorOption, titleString, descString, dueDateString, dateCreatedString;
    String dueDateStringFormat, dateCreatedStringFormat;

    EditText goalTitle, goalDesc, goalDueDate;
    Button cancelButton, addGoalButton;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar = Calendar.getInstance();

    private DBManager dbManager;

    String daysLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        //setTitle("Add Goal");
        //getSupportActionBar().setElevation(0);

        colorOption = "#FF0000";

        dbManager = new DBManager(this);
        dbManager.open();

        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        goalTitle = (EditText) findViewById(R.id.editTextTitle);
        goalDesc = (EditText) findViewById(R.id.editTextDescription);
        goalDueDate = (EditText) findViewById(R.id.editTextDueDate);
        cancelButton = (Button) findViewById(R.id.cancelGoalButton);
        addGoalButton = (Button) findViewById(R.id.addGoalButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                if(checkedId == R.id.red) {
                    colorOption = "#96ff0000";
                } else if(checkedId == R.id.green) {
                    colorOption = "#96008000";
                } else if(checkedId == R.id.yellow) {
                    colorOption = "#96ffff00";
                } else if(checkedId == R.id.grey) {
                    colorOption = "#96808080";
                } else if(checkedId == R.id.blue) {
                    colorOption = "#960000ff";
                } else if(checkedId == R.id.purple) {
                    colorOption = "#96800080";
                }
            }

        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        goalDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGoal.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGoal.this.finish();
            }
        });

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_into_database();
                Intent intent = new Intent(AddGoal.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "EEEE, dd MMMM, yyyy";
        String newFormat = "MM/dd/yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfnewFormat = new SimpleDateFormat(newFormat, Locale.US);

        goalDueDate.setText(sdf.format(myCalendar.getTime()));
        dueDateString = sdf.format(myCalendar.getTime());
        dueDateStringFormat = sdfnewFormat.format(myCalendar.getTime());
    }

    public void insert_into_database() {


        Date c = Calendar.getInstance().getTime();
        String myFormat = "EEEE, dd MMMM, yyyy";
        String newFormat2 = "MM/dd/yyyy";
        SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat dfnewFormat = new SimpleDateFormat(newFormat2, Locale.US);
        dateCreatedString = df.format(c);
        dateCreatedStringFormat = dfnewFormat.format(c);

        titleString = goalTitle.getText().toString();
        descString = goalDesc.getText().toString();

        String status = "Pending";

        dbManager.insert_a_goal(titleString, descString, colorOption, dueDateString, dueDateStringFormat,
                dateCreatedString, dateCreatedStringFormat, status);


    }
}
