package ng.startup.journeyapp.prompt;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.database.DBManager;
import ng.startup.journeyapp.fragments.goals.GoalsClass;

/**
 * Created by mungwaagu on 21/11/2018.
 */

public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<GoalsClass> goalsList;
    private DBManager dbManager;

    public PromptAdapter(Context context, List<GoalsClass> goalsList) {
        this.inflater = LayoutInflater.from(context);
        this.goalsList = goalsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.prompt_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.goalTitle.setText(goalsList.get(position).getGoalTitle());
        int red = Color.parseColor(goalsList.get(position).getGoalColor());
        holder.background.setBackgroundColor(red);
        holder.prompt_card.setCardBackgroundColor(red);

        holder.stepField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final int position2 = holder.stepField.getId();
                final EditText stepCaption = (EditText) holder.stepField;
                if(stepCaption.getText().toString().length() > 0){
                    String stepTitle = stepCaption.getText().toString();
                    String goalTitle = goalsList.get(position).getGoalTitle();
                    String goalColor = goalsList.get(position).getGoalColor();

                    Date c = Calendar.getInstance().getTime();
                    String myFormat = "EEEE, dd MMMM, yyyy";
                    SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);
                    String dateCreated = df.format(c);

                    String myFormat2 = "dd,MM,yyyy,MMM";
                    SimpleDateFormat df2 = new SimpleDateFormat(myFormat2, Locale.US);
                    String dateCreatedFormat = df2.format(c);


                        holder.saveStep.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbManager = new DBManager(v.getContext());
                                dbManager.open();

                                dbManager.insert_a_step(stepTitle, dateCreated, dateCreatedFormat,
                                        goalTitle, goalColor,"","Pending");
                                //Toast.makeText(v.getContext(), stepTitle, Toast.LENGTH_SHORT).show();
                                goalsList.remove(position);
                                notifyItemRemoved(position);

                            }
                        });


                }else if (stepCaption.getText().toString().length() == 0) {
                    holder.saveStep.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "No Value Entered", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }


    public String getGoalDetails(int position) {

        if(position < goalsList.size()) {
            return goalsList.get(position).getGoalTitle();
        }
        return "Finished";
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView goalTitle;
        EditText stepField;
        CardView prompt_card;
        LinearLayout background;
        Button saveStep;
        ViewHolder(View view) {
            super(view);

            goalTitle = (TextView) view.findViewById(R.id.textGoalTitle);
            stepField = (EditText) view.findViewById(R.id.editTextStep);
            saveStep = (Button) view.findViewById(R.id.buttonSubmitStep);
            background = (LinearLayout) view.findViewById(R.id.card_background);
            prompt_card = (CardView) view.findViewById(R.id.prompt_card);

        }
    }

}
