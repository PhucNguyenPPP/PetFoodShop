package com.example.petshopproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM Food WHERE status = 1")
    List<Food> getAll();

    @Query("SELECT * FROM Food WHERE foodId = :foodId")
    Food getFoodById(String foodId);

    @Insert
    void insert(Food food);

    @Update
    void update (Food food);

    @Query("UPDATE Food SET status = 0 WHERE foodId = :foodId")
    void delete(String foodId);
    @Query("SELECT * FROM Food WHERE foodId = :foodId")
    LiveData<Food> getFoodByIdLiveData(String foodId);
}
