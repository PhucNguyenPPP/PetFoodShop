package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pet {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "petId")
    private String petId;

    @NonNull
    @ColumnInfo(name = "petName")
    private String petName;


    public Pet() {
    }

    public Pet(@NonNull String petId, @NonNull String petName) {
        this.petId = petId;
        this.petName = petName;
    }

    @NonNull
    public String getPetId() {
        return petId;
    }

    public void setPetId(@NonNull String petId) {
        this.petId = petId;
    }

    @NonNull
    public String getPetName() {
        return petName;
    }

    public void setPetName(@NonNull String petName) {
        this.petName = petName;
    }
}
