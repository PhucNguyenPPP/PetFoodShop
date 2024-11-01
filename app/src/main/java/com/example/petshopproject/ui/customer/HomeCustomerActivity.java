package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.FoodCustomerAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Cart;
import com.example.petshopproject.model.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dao.CartDao;

public class HomeCustomerActivity extends AppCompatActivity {

    AppDatabase mDb;
    ListView lvFood;
    List<Food> arrayFood;
    FoodCustomerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();

        lvFood = findViewById(R.id.lvFood);
        arrayFood = new ArrayList<>();
        adapter = new FoodCustomerAdapter(this, R.layout.row_food_customer, arrayFood);
        lvFood.setAdapter(adapter);

        GetDataFood();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.customer_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_customer){

        } else if (item.getItemId() == R.id.navigation_order_customer){
            Intent intent = new Intent(HomeCustomerActivity.this, OrderCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_cart){
            Intent intent = new Intent(HomeCustomerActivity.this, CartCustomerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(HomeCustomerActivity.this);
        return sessionManager.getUserId();
    }

    private void GetDataFood(){
        arrayFood.clear();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Food> foods = mDb.foodDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayFood.clear();
                        arrayFood.addAll(foods);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void AddToCart(String foodId, String userId, int availableAmount) {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            Cart existingItem = mDb.cartDao().getCartByFoodAndUser(foodId,userId);
            if (existingItem != null) {
                // Item exists, update quantity
                if(existingItem.getAmount() + 1>availableAmount)
                {
                    runOnUiThread(() -> Toast.makeText(HomeCustomerActivity.this, "Số lượng sản phẩm bạn chọn vượt quá số lượng có sẵn!", Toast.LENGTH_SHORT).show());
                } else {
                    existingItem.setAmount(existingItem.getAmount() + 1);
                    mDb.cartDao().update(existingItem);
                    runOnUiThread(() -> Toast.makeText(HomeCustomerActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show());
                }
            } else {
                // Item doesn't exist, add new entry
                String cartid = UUID.randomUUID().toString();
                Cart newCartItem = new Cart(cartid,userId,foodId,1);
                mDb.cartDao().insert(newCartItem);
                runOnUiThread(() -> Toast.makeText(HomeCustomerActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show());
            }
        });
    }

}