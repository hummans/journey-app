package ng.startup.journeyapp.fragments.today;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ng.startup.journeyapp.R;

import java.util.ArrayList;

/**
 * Created by mungwaagu on 29/10/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StepsClass> stepsList;
    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    public StepsAdapter (Context context, ArrayList<StepsClass> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent,false);

        return new ViewHolder(itemView);
    }

    public void remove(int position) {

        stepsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.ViewHolder holder, final int position) {

        holder.goalTitle.setText(stepsList.get(position).getGoalTitle());
        holder.stepTitle.setText(stepsList.get(position).getStepTitle());

        String status = stepsList.get(position).getStepStatus();
        if(status.equalsIgnoreCase("Done")){

            holder.imageDone.setVisibility(View.VISIBLE);
            holder.imagePending.setVisibility(View.INVISIBLE);

        }else if(status.equalsIgnoreCase("Pending")){

            holder.imageDone.setVisibility(View.INVISIBLE);
            holder.imagePending.setVisibility(View.VISIBLE);

        }

        int red = Color.parseColor(stepsList.get(position).getGoalColor());
        holder.sideColor.setBackgroundColor(red);

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        public TextView goalTitle;
        public TextView stepTitle;
        public ImageView imageDone;
        public ImageView imagePending;
        public RelativeLayout sideColor;

        public ViewHolder(View itemView) {
            super(itemView);
            goalTitle = (TextView) itemView.findViewById(R.id.goalTitle);
            stepTitle = (TextView) itemView.findViewById(R.id.stepTitle);
            sideColor = (RelativeLayout) itemView.findViewById(R.id.sideColor2);
            imageDone = (ImageView) itemView.findViewById(R.id.imageDone);
            imagePending = (ImageView) itemView.findViewById(R.id.imagePending);

        }
    }

}