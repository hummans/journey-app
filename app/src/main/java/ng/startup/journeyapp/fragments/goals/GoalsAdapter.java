package ng.startup.journeyapp.fragments.goals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ng.startup.journeyapp.R;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder>{

    private Context context;
    private List<GoalsClass> goalsList;
    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    public GoalsAdapter (Context context, List<GoalsClass> goalsList) {
        this.context = context;
        this.goalsList = goalsList;
    }

    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_card_item, parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoalsAdapter.ViewHolder holder, final int position) {

        holder.goalNoOfDaysLeft.setText(goalsList.get(position).getGoalDaysLeft());
        holder.goalTitle.setText(goalsList.get(position).getGoalTitle());
        holder.noOfSteps.setText(goalsList.get(position).getGoalNoSteps());
        int red = Color.parseColor(goalsList.get(position).getGoalColor());
        holder.goalCard.setCardBackgroundColor(red);
        holder.background.setBackgroundColor(red);

        holder.goalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoalIndividual.class);
                intent.putExtra("goalId",goalsList.get(position).getGoalId());
                intent.putExtra("goalTitle",goalsList.get(position).getGoalTitle());
                intent.putExtra("goalDescription",goalsList.get(position).getGoalDescription());
                intent.putExtra("goalColor",goalsList.get(position).getGoalColor());
                intent.putExtra("goalDueDate",goalsList.get(position).getGoalDueDate());
                intent.putExtra("goalDueDateFormat",goalsList.get(position).getGoalDueDateFormat());
                intent.putExtra("goalDateCreated",goalsList.get(position).getGoalDateCreated());
                intent.putExtra("goalDateCreatedFormat",goalsList.get(position).getGoalDateCreatedFormat());
                intent.putExtra("goalDaysLeft",goalsList.get(position).getGoalDaysLeft());
                intent.putExtra("goalStatus",goalsList.get(position).getGoalStatus());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        public TextView goalNoOfDaysLeft;
        public TextView goalTitle;
        public TextView noOfSteps;
        public CardView goalCard;
        public RelativeLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            goalNoOfDaysLeft = (TextView) itemView.findViewById(R.id.noOfDays);
            goalTitle = (TextView) itemView.findViewById(R.id.goalTitle);
            goalCard = (CardView) itemView.findViewById(R.id.goal_card);
            noOfSteps = (TextView) itemView.findViewById(R.id.noOfSteps);
            background = (RelativeLayout) itemView.findViewById(R.id.goalBackground);

        }
    }
}
