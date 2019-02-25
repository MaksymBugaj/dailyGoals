package com.example.maksy.dailygoals.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.maksy.dailygoals.data.model.Goal;

@Database(entities = Goal.class, version = 1)
public abstract class GoalDatabase extends RoomDatabase {

    private static GoalDatabase instance;

    public abstract GoalDao goalDao();

    public static synchronized GoalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GoalDatabase.class, "daily_goals")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private GoalDao goalDao;

        public PopulateDbAsyncTask(GoalDatabase goalDatabase) {
            goalDao = goalDatabase.goalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
