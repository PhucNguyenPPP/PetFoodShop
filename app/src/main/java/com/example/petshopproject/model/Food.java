package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Food {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @NonNull
    @ColumnInfo(name = "foodName")
    private String foodName;

    @NonNull
    @ColumnInfo(name = "imageLink")
    private String imageLink;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "price")
    private float price;

    @NonNull
    @ColumnInfo(name = "weight")
    private float weight;

    @NonNull
    @ColumnInfo(name = "amountInStock")
    private int amountInStock;

    @NonNull
    @ColumnInfo(name = "status")
    private boolean status;

    public Food() {
    }

    public Food(@NonNull String foodId, @NonNull String foodName, @NonNull String imageLink, @NonNull String description, float price, float weight, int amountInStock, boolean status) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.imageLink = imageLink;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.amountInStock = amountInStock;
        this.status = status;
    }

    @NonNull
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(@NonNull String foodId) {
        this.foodId = foodId;
    }

    @NonNull
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(@NonNull String foodName) {
        this.foodName = foodName;
    }

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
