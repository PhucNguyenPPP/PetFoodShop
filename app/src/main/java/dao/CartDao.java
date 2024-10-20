package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM Cart")
    List<Cart> getAll();

    @Insert
    void insert(Cart cart);

    @Update
    void update (Cart cart);

    @Delete
    void delete(Cart cart);
}
