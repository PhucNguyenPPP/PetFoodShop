package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Role.class,
                parentColumns = "roleId",
                childColumns = "roleId"
        )
)
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @NonNull
    @ColumnInfo(name = "userName")
    private String userName;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    @ColumnInfo(name = "fullName")
    private String fullName;

    @NonNull
    @ColumnInfo(name = "phone")
    private String phone;

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @NonNull
    @ColumnInfo(name = "status")
    private boolean status;

    @ColumnInfo(name = "roleId")
    @NonNull
    private String roleId;

    public User() {
    }

    public User(@NonNull String userId, @NonNull String userName, @NonNull String password, @NonNull String fullName, @NonNull String phone, @NonNull String email, boolean status, @NonNull String roleId) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.roleId = roleId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @NonNull
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(@NonNull String roleId) {
        this.roleId = roleId;
    }
}
