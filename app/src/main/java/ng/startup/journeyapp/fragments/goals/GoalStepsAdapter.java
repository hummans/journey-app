package ng.startup.journeyapp.fragments.goals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.fragments.today.StepsClass;

import java.util.ArrayList;

/**
 * Created by mungwaagu on 29/10/2018.
 */

public class GoalStepsAdapter extends RecyclerView.Adapter<GoalStepsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StepsClass> stepsList;
    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    public GoalStepsAdapter(Context context, ArrayList<StepsClass> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_step_card_item, parent,false);

        return new ViewHolder(itemView);
    }

    public void remove(int position) {
        stepsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String hasJournal = stepsList.get(position).getStepJournal();

        if(hasJournal.equalsIgnoreCase("")){
            holder.imageJournal.setVisibility(View.INVISIBLE);
        }else if(!hasJournal.equalsIgnoreCase("")){
            holder.imageJournal.setVisibility(View.VISIBLE);
        }

        String date = stepsList.get(position).getStepDateCreatedFormat();
        String parts[] = date.split(",");

        String day = parts[0];
        String month = parts[3];

        holder.stepDay.setText(day);
        holder.stepMonth.setText(month);
        holder.stepTitle.setText(stepsList.get(position).getStepTitle());

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        public TextView stepDay;
        public TextView stepMonth;
        public TextView stepTitle;
        public ImageView imageJournal;

        public ViewHolder(View itemView) {
            super(itemView);
            stepDay = (TextView) itemView.findViewById(R.id.textDay);
            stepMonth = (TextView) itemView.findViewById(R.id.textMonth);
            stepTitle = (TextView) itemView.findViewById(R.id.stepTitle);
            imageJournal = (ImageView) itemView.findViewById(R.id.imageJournal);

        }
    }

}