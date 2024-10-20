package com.example.petshopproject.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Order.class,
                        parentColumns = "orderId",
                        childColumns = "orderId"
                ),
                @ForeignKey(
                        entity = Food.class,
                        parentColumns = "foodId",
                        childColumns = "foodId"
                )
        })
public class OrderDetail {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "orderDetailId")
    private String orderDetailId;

    @NonNull
    @ColumnInfo(name = "orderId")
    private String orderId;

    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @NonNull
    @ColumnInfo(name = "amount")
    private int amount;

    @NonNull
    @ColumnInfo(name = "price")
    private float price;

    public OrderDetail() {
    }

    public OrderDetail(@NonNull String orderDetailId, @NonNull String orderId, @NonNull String foodId, int amount, float price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.amount = amount;
        this.price = price;
    }

    @NonNull
    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(@NonNull String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    @NonNull
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {
        this.orderId = orderId;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
