package ng.startup.journeyapp.reflect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.fragments.goals.GoalsClass;
import ng.startup.journeyapp.fragments.today.StepIndividual;
import ng.startup.journeyapp.fragments.today.StepsClass;
import ng.startup.journeyapp.prompt.PromptAdapter;

/**
 * Created by mungwaagu on 21/11/2018.
 */

public class ReflectAdapter extends RecyclerView.Adapter<ReflectAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<StepsClass> stepsList;

    public ReflectAdapter (Context context, List<StepsClass> stepsList) {
        this.inflater = LayoutInflater.from(context);
        this.stepsList = stepsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.reflect_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReflectAdapter.ViewHolder holder, int position) {

        holder.goalTitle.setText(stepsList.get(position).getGoalTitle());
        holder.stepTitle.setText(stepsList.get(position).getStepTitle());
        int red = Color.parseColor(stepsList.get(position).getGoalColor());
        holder.background.setBackgroundColor(red);
        holder.reflect_card.setCardBackgroundColor(red);

        holder.writeJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReflectStepIndividual.class);
                intent.putExtra("stepId",stepsList.get(position).getStepId());
                intent.putExtra("stepTitle",stepsList.get(position).getStepTitle());
                intent.putExtra("stepDateCreated",stepsList.get(position).getStepDateCreated());
                intent.putExtra("stepDateCreatedFormat",stepsList.get(position).getStepDateCreatedFormat());
                intent.putExtra("goalColor",stepsList.get(position).getGoalColor());
                intent.putExtra("goalTitle",stepsList.get(position).getGoalTitle());
                intent.putExtra("stepJournal",stepsList.get(position).getStepJournal());
                intent.putExtra("stepStatus",stepsList.get(position).getStepStatus());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepTitle;
        TextView goalTitle;
        CardView reflect_card;
        LinearLayout background;
        Button writeJournal;
        ViewHolder(View view) {
            super(view);

            stepTitle = (TextView) view.findViewById(R.id.textStepTitle);
            goalTitle = (TextView) view.findViewById(R.id.textGoalTitle);
            background = (LinearLayout) view.findViewById(R.id.card_background);
            reflect_card = (CardView) view.findViewById(R.id.reflect_card);
            writeJournal = (Button) view.findViewById(R.id.buttonGotoJournal);

        }
    }

}
