package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Pet;

import java.util.List;

@Dao
public interface PetDao {
    @Query("SELECT * FROM Pet")
    List<Pet> getAll();

    @Query("SELECT * FROM Pet WHERE petId = :petId")
    Pet getPetById(String petId);

    @Insert
    void insert(Pet pet);

    @Update
    void update (Pet pet);

    @Delete
    void delete(Pet pet);
}
