package com.example.petshopproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Query("SELECT * FROM OrderDetail")
    List<OrderDetail> getAll();

    @Insert
    void insert(OrderDetail orderDetail);

    @Update
    void update (OrderDetail orderDetail);

    @Delete
    void delete(OrderDetail orderDetail);
}
