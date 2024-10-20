package com.example.petshopproject.adapter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.petshopproject.dao.CartDao;
import com.example.petshopproject.dao.FoodDao;
import com.example.petshopproject.dao.OrderDao;
import com.example.petshopproject.dao.OrderDetailDao;
import com.example.petshopproject.dao.PetDao;
import com.example.petshopproject.dao.RoleDao;
import com.example.petshopproject.dao.UserDao;
import com.example.petshopproject.model.Cart;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.FoodPet;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.OrderDetail;
import com.example.petshopproject.model.Pet;
import com.example.petshopproject.model.Role;
import com.example.petshopproject.model.User;

import dao.FoodPetDao;

@Database(entities = {
        Role.class,
        User.class,
        Pet.class,
        Food.class,
        FoodPet.class,
        Cart.class,
        Order.class,
        OrderDetail.class
},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoleDao roleDao();
    public abstract UserDao userDao();
    public abstract PetDao petDao();
    public abstract FoodDao foodDao();
    public abstract FoodPetDao foodPetDao();
    public abstract CartDao cartDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();
}
