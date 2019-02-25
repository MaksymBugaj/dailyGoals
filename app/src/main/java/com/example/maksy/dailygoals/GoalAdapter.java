package com.example.maksy.dailygoals;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maksy.dailygoals.data.model.Goal;

public class GoalAdapter extends ListAdapter<Goal, GoalAdapter.GoalHolder> {
    private OnItemClickListener listener;
    private Context context;

    public GoalAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }


    private static final DiffUtil.ItemCallback<Goal> DIFF_CALLBACK = new DiffUtil.ItemCallback<Goal>() {
        @Override
        public boolean areItemsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Goal oldItem, @NonNull Goal newItem) {
            return oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    @NonNull
    @Override
    public GoalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goal_item, viewGroup, false);
        return new GoalHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalHolder goalHolder, int i) {
        Goal currentGoal = getItem(i);
        goalHolder.textViewGoal.setText(currentGoal.getDescription());
        goalHolder.textViewPrior.setText(String.valueOf(currentGoal.getPriority()));
        if(goalHolder.textViewPrior.getText().toString().equals("1"))goalHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.RED));
        if(goalHolder.textViewPrior.getText().toString().equals("3"))goalHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.BLUE));
        if(goalHolder.textViewPrior.getText().toString().equals("5"))goalHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.GREEN));
    }

    public Goal getGoalAt(int pos) {
        return getItem(pos);
    }


    class GoalHolder extends RecyclerView.ViewHolder {
        private TextView textViewGoal;
        private TextView textViewPrior;
        private RelativeLayout relativeLayout;

        public GoalHolder(@NonNull View itemView) {
            super(itemView);
            textViewGoal = itemView.findViewById(R.id.text_view_goal);
            textViewPrior = itemView.findViewById(R.id.text_view_prior);
            relativeLayout = itemView.findViewById(R.id.goal_item_layout);
            //relativeLayout.getBackground().setColorFilter(Color.parseColor("#00ff00"),PorterDuff.Mode.DARKEN);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Goal goal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
