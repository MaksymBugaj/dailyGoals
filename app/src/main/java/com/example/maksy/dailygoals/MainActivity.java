package com.example.maksy.dailygoals;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.maksy.dailygoals.data.model.Goal;
import com.example.maksy.dailygoals.data.viewmodel.GoalViewModel;
import com.example.maksy.dailygoals.ui.AddEditGoalActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GoalViewModel goalViewModel;
    public static final int ADD_GOAL_REQUEST = 1;
    public static final int EDIT_GOAL_REQUEST = 2;
    private static final String PREFERENCES_NAME = "myPreferences";

    private boolean mOneGoalTaken;
    private boolean mThreeGoalsTaken;
    private boolean mFiveGoalsTaken;
    private int mOneGoalCounter = 0;
    private int mThreeGoalsCounter = 0;
    private int mFiveGoalsCounter = 0;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());
        setTitle("Daily Goals for: " +currentDate);

        sharedPref = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditGoalActivity.class);
                intent.putExtra("flag1Status", "taken");
                startActivityForResult(intent, ADD_GOAL_REQUEST);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final GoalAdapter adapter = new GoalAdapter(this);
        recyclerView.setAdapter(adapter);

        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        goalViewModel.getAllGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                adapter.submitList(goals);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                goalViewModel.delete(adapter.getGoalAt(viewHolder.getAdapterPosition()));
                int prior = adapter.getGoalAt(viewHolder.getAdapterPosition()).getPriority();
                editor = sharedPref.edit();
                if(prior == 1){
                    mOneGoalCounter--;
                    editor.putInt("mOneGoalCounter", mOneGoalCounter);
                }
                if(prior == 3){
                    mThreeGoalsCounter--;
                    editor.putInt("mThreeGoalsCounter", mThreeGoalsCounter);
                }
                if(prior == 5){
                    mFiveGoalsCounter--;
                    editor.putInt("mFiveGoalsCounter", mFiveGoalsCounter);
                }
                editor.apply();
                Toast.makeText(MainActivity.this, "Goal Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new GoalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Goal goal) {
                Intent intent = new Intent(MainActivity.this, AddEditGoalActivity.class);
                intent.putExtra(AddEditGoalActivity.EXTRA_ID, goal.getId());
                intent.putExtra(AddEditGoalActivity.EXTRA_GOAL, goal.getDescription());
                intent.putExtra(AddEditGoalActivity.EXTRA_PRIORITY, goal.getDescription());
                startActivityForResult(intent, EDIT_GOAL_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GOAL_REQUEST && resultCode == RESULT_OK && data != null) {
            String desc = data.getStringExtra(AddEditGoalActivity.EXTRA_GOAL);
            int prior = data.getIntExtra(AddEditGoalActivity.EXTRA_PRIORITY, 1);
            mOneGoalCounter = sharedPref.getInt("mOneGoalCounter", 0);
            mThreeGoalsCounter = sharedPref.getInt("mThreeGoalsCounter", 0);
            mFiveGoalsCounter = sharedPref.getInt("mFiveGoalsCounter", 0);
            if (mOneGoalCounter == 1) {
                mOneGoalTaken = true;
            } else {
                mOneGoalTaken = false;
            }
            if (mThreeGoalsCounter == 3) {
                mThreeGoalsTaken = true;
            }else {
                mThreeGoalsTaken = false;
            }
            if (mFiveGoalsCounter == 5) {
                mFiveGoalsTaken = true;
            }else {
                mFiveGoalsTaken = false;
            }

            if (prior == 1) {
                mOneGoalCounter++;
            } else if (prior == 3) {
                mThreeGoalsCounter++;
            } else if (prior == 5) {
                mFiveGoalsCounter++;
            }
            if (mOneGoalTaken && prior == 1) {
                Toast.makeText(this, "Only one goal with max priority per day!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mThreeGoalsTaken && prior == 3) {
                Toast.makeText(this, "Only 3 goals with medium priority per day!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mFiveGoalsTaken && prior == 5) {
                Toast.makeText(this, "Only 5 goals with low priority per day!", Toast.LENGTH_SHORT).show();
                return;
            }
            editor = sharedPref.edit();
            editor.putInt("mOneGoalCounter", mOneGoalCounter);
            editor.putInt("mThreeGoalsCounter", mThreeGoalsCounter);
            editor.putInt("mFiveGoalsCounter", mFiveGoalsCounter);
            editor.apply();
            Goal goal = new Goal(desc, prior);
            goalViewModel.insert(goal);
            Toast.makeText(this, "Goal saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_GOAL_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditGoalActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Goal cant be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String goal = data.getStringExtra(AddEditGoalActivity.EXTRA_GOAL);
            int prior = Integer.parseInt(data.getStringExtra(AddEditGoalActivity.EXTRA_PRIORITY));
            Goal goal1 = new Goal(goal, prior);
            goal1.setId(id);
            goalViewModel.update(goal1);
            Toast.makeText(this, "Goal updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Goal not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_goals:
                goalViewModel.deleteAllGoals();
                Toast.makeText(this, "All goals deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
