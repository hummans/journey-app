package ng.startup.journeyapp.fragments.today;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBManager;

public class StepIndividual extends AppCompatActivity {

    String stepTitle, stepDateCreated, stepDateCreatedFormat, goalTitle, goalColor, stepJournal, stepStatus;
    int stepID;

    TextView dateCreatedText;
    EditText stepTitleText, stepJournalText;
    Button saveStep;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_individual);

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
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(goalColor)));

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
                //Intent intent = new Intent(StepIndividual.this, HomeScreen.class);
                //startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.step_individual_menu, menu);
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

}
