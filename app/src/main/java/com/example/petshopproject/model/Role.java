package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Role {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "roleId")
    private String roleId;

    @ColumnInfo(name = "roleName")
    @NonNull
    private String roleName;

    public Role() {
    }

    public Role(@NonNull String roleId, @NonNull String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @NonNull
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(@NonNull String roleId) {
        this.roleId = roleId;
    }

    @NonNull
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(@NonNull String roleName) {
        this.roleName = roleName;
    }
}
