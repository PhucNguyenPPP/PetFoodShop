package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("SELECT * FROM Food WHERE foodId = :foodId")
    Food getFoodById(String foodId);

    @Insert
    void insert(Food food);

    @Update
    void update (Food food);

    @Delete
    void delete(Food food);
}
