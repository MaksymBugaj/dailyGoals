package com.example.maksy.dailygoals.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.maksy.dailygoals.data.model.Goal;
import com.example.maksy.dailygoals.data.repository.GoalRepository;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;


    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
        allGoals = goalRepository.getAllGoals();
    }

    public void insert(Goal goal) {
        goalRepository.insert(goal);
    }

    public void update(Goal goal){
        goalRepository.update(goal);
    }

    public void delete(Goal goal){
        goalRepository.delete(goal);
    }

    public void deleteAllGoals(){
        goalRepository.deleteAllGoals();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }
}
