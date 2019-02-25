package com.example.maksy.dailygoals.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maksy.dailygoals.data.model.Goal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface GoalDao {

    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("delete from goal_table")
    void deleteAllGoals();

    @Query("select * from goal_table")
    LiveData<List<Goal>> getAllGoales();

}
