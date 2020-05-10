package ng.startup.journeyapp.reflect;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBManager;

public class ReflectStepIndividual extends AppCompatActivity {

    String stepTitle, stepDateCreated, stepDateCreatedFormat, goalTitle, goalColor, stepJournal, stepStatus;
    int stepID;

    TextView dateCreatedText;
    EditText stepTitleText, stepJournalText;
    Button saveStep;

    private DBManager dbManager;

    String dayDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect_step_individual);
        dbManager = new DBManager(this);
        dbManager.open();


        dateCreatedText = (TextView) findViewById(R.id.stepDateCreated);
        stepTitleText = (EditText) findViewById(R.id.stepTitle);
        stepJournalText = (EditText) findViewById(R.id.stepJournal);
        saveStep = (Button) findViewById(R.id.saveStep);


        Bundle extras = getIntent().getExtras();
        stepID = extras.getInt("stepId");
        stepTitle = extras.getString("stepTitle");
        stepDateCreated = extras.getString("stepDateCreated");
        stepDateCreatedFormat = extras.getString("stepDateCreatedFormat");
        goalColor = extras.getString("goalColor");
        goalTitle = extras.getString("goalTitle");
        stepJournal = extras.getString("stepJournal");
        stepStatus = extras.getString("stepStatus");

        getSupportActionBar().setTitle(goalTitle);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(goalColor)));

        dateCreatedText.setText(stepDateCreated);
        stepTitleText.setText(stepTitle);
        stepJournalText.setText(stepJournal);

        saveStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newStepTitle = stepTitleText.getText().toString();
                String newStepJournal = stepJournalText.getText().toString();

                dbManager.update_in_steps(stepID, newStepTitle, stepDateCreated, stepDateCreatedFormat,
                        goalTitle, goalColor, newStepJournal, stepStatus);

                Toast.makeText(getApplicationContext(), "Saved Succesfully", Toast.LENGTH_SHORT).show();
                ReflectStepIndividual.this.finish();
                //Intent intent = new Intent(StepIndividual.this, HomeScreen.class);
                //startActivity(intent);

            }
        });

    }
}
