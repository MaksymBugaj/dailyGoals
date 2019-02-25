package com.example.maksy.dailygoals.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.maksy.dailygoals.R;

public class AddEditGoalActivity extends AppCompatActivity {
    public static final String TAG = "AddEditGoalActivity";
    public static final String EXTRA_ID =
            "com.example.maksy.testing.EXTRA_ID";
    public static final String EXTRA_GOAL =
            "com.example.maksy.testing.EXTRA_GOAL";
    public static final String EXTRA_PRIORITY =
            "com.example.maksy.testing.EXTRA_PRIORITY";

    private EditText editTextGoal;
    private NumberPicker numberPicker;
    private String[] values = {"1","3","5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_goal);

        editTextGoal = findViewById(R.id.edit_text_goal);
        numberPicker = findViewById(R.id.number_picker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(values.length-1);
        numberPicker.setDisplayedValues(values);
        numberPicker.setWrapSelectorWheel(true);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Goal");
            editTextGoal.setText(intent.getStringExtra(EXTRA_GOAL));
            numberPicker.setValue(Integer.parseInt(intent.getStringExtra(EXTRA_PRIORITY)));
        } else {
            setTitle("Add Goal");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_goal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_goal:
                saveGoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveGoal(){
        String goal = editTextGoal.getText().toString();
        int pos = numberPicker.getValue();
        String val = values[pos];
        int prior = Integer.parseInt(val);
        Log.d(TAG, "saveGoal: prior? " +prior + "val: " + val + "pos: " + pos);


        if (goal.trim().isEmpty()) {
            Toast.makeText(this, "Please insert bla bla", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_GOAL, goal);
        data.putExtra(EXTRA_PRIORITY, prior);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
