package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Food.class,
                        parentColumns = "foodId",
                        childColumns = "foodId"
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId"
                )
        }
)
public class Cart {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cartId")
    private String cartId;

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @NonNull
    @ColumnInfo(name = "amount")
    private int amount;

    public Cart() {
    }

    public Cart(@NonNull String cartId, @NonNull String userId, @NonNull String foodId, int amount) {
        this.cartId = cartId;
        this.userId = userId;
        this.foodId = foodId;
        this.amount = amount;
    }

    @NonNull
    public String getCartId() {
        return cartId;
    }

    public void setCartId(@NonNull String cartId) {
        this.cartId = cartId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(@NonNull String foodId) {
        this.foodId = foodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
