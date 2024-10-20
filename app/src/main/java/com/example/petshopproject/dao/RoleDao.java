package com.example.petshopproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petshopproject.model.Role;

import java.util.List;

@Dao
public interface RoleDao {
    @Query("SELECT * FROM Role")
    List<Role> getAll();

    @Query("SELECT * FROM Role WHERE roleId = :roleId")
    Role getRoleById(String roleId);

    @Insert
    void insert(Role role);

    @Update
    void update (Role role);

    @Delete
    void delete(Role role);

    @Query("SELECT * FROM Role WHERE roleName = 'Customer'")
    Role getCustomerRole();

    @Query("SELECT * FROM Role WHERE roleName = 'Shipper'")
    Role getShipperRole();

    @Query("SELECT * FROM Role WHERE roleName = 'Admin'")
    Role getAdminRole();
}
