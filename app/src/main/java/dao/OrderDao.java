package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM `Order`")
    List<Order> getAll();

    @Query("SELECT * FROM `Order` WHERE orderId = :orderId")
    Order getOrderById(String orderId);

    @Insert
    void insert(Order order);

    @Update
    void update (Order order);

    @Delete
    void delete(Order order);
}
