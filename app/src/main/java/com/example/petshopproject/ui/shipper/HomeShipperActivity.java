package com.example.petshopproject.ui.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.FoodCustomerAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.ui.customer.CartCustomerActivity;
import com.example.petshopproject.ui.customer.HomeCustomerActivity;
import com.example.petshopproject.ui.customer.OrderCustomerActivity;

import java.util.ArrayList;

public class HomeShipperActivity extends AppCompatActivity {

    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_shipper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.shipper_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_shipper){

        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(HomeShipperActivity.this);
        return sessionManager.getUserId();
    }
}
