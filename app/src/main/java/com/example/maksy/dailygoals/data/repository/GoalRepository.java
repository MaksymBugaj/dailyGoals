package com.example.maksy.dailygoals.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maksy.dailygoals.data.db.GoalDao;
import com.example.maksy.dailygoals.data.db.GoalDatabase;
import com.example.maksy.dailygoals.data.model.Goal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GoalRepository {
    public static final String TAG = "iCHUJ";
    private GoalDao goalDao;
    private LiveData<List<Goal>> allGoals;
    private CompositeDisposable disposable = new CompositeDisposable();

    public GoalRepository(Application application){
        GoalDatabase goalDatabase = GoalDatabase.getInstance(application);
        goalDao = goalDatabase.goalDao();
        allGoals = goalDao.getAllGoales();
    }

    public void insert(Goal goal){
        new InsertGoalAsyncTask(goalDao).execute(goal);
    }


    public void update(Goal goal) { new UpdateGoalAsyncTask(goalDao).execute(goal);}

    public void delete(Goal goal) { new DeleteGoalAsyncTask(goalDao).execute(goal);}

    public void deleteAllGoals(){ new DeleteAllGoalsAsyncTask(goalDao).execute();}

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    private static class InsertGoalAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;

        public InsertGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.insert(goals[0]);
            return null;
        }
    }

    private static class UpdateGoalAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;

        public UpdateGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.update(goals[0]);
            return null;
        }
    }

    private static class DeleteGoalAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;

        public DeleteGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.delete(goals[0]);
            return null;
        }
    }

    private static class DeleteAllGoalsAsyncTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao goalDao;

        public DeleteAllGoalsAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.deleteAllGoals();
            return null;
        }
    }
 }
