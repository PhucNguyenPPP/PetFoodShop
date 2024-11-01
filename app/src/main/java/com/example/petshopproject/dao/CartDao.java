package com.example.petshopproject.dao;

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
    @Query("SELECT * FROM Cart WHERE foodId = :foodId AND userId = :userId")
    Cart getCartByFoodAndUser(String foodId, String userId);
    @Query("SELECT * FROM Cart WHERE userId = :userId")
    List<Cart> getCartByUser(String userId);
    @Query("SELECT * FROM Cart WHERE cartId = :cartId")
    List<Cart> getCartByCartId(String cartId);
}
