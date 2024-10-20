package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.FoodPet;

import java.util.List;

@Dao
public interface FoodPetDao {
    @Query("SELECT * FROM FoodPet")
    List<FoodPet> getAll();

    @Insert
    void insert(FoodPet foodPet);

    @Update
    void update (FoodPet foodPet);

    @Delete
    void delete(FoodPet foodPet);

    @Query("SELECT * FROM FoodPet WHERE foodId = :foodId")
    List<FoodPet> getFoodPetsForFood(String foodId);
    @Query("DELETE FROM FoodPet WHERE foodId = :foodId")
    void deleteFoodPetByFoodId(String foodId);
}
