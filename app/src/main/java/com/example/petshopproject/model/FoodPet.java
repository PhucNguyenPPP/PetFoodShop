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
                        entity = Pet.class,
                        parentColumns = "petId",
                        childColumns = "petId"
                )
        }
)
public class FoodPet {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodPetId")
    private String foodPetId;

    @NonNull
    @ColumnInfo(name = "foodId")
    private String foodId;

    @NonNull
    @ColumnInfo(name = "petId")
    private String petId;

    public FoodPet() {
    }

    public FoodPet(@NonNull String foodPetId, @NonNull String foodId, @NonNull String petId) {
        this.foodPetId = foodPetId;
        this.foodId = foodId;
        this.petId = petId;
    }

    @NonNull
    public String getFoodPetId() {
        return foodPetId;
    }

    public void setFoodPetId(@NonNull String foodPetId) {
        this.foodPetId = foodPetId;
    }

    @NonNull
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(@NonNull String foodId) {
        this.foodId = foodId;
    }

    @NonNull
    public String getPetId() {
        return petId;
    }

    public void setPetId(@NonNull String petId) {
        this.petId = petId;
    }
}
