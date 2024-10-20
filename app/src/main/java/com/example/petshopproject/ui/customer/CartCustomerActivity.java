package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.SessionManager;

public class CartCustomerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.customer_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_customer){
            Intent intent = new Intent(CartCustomerActivity.this, HomeCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_order_customer){
            Intent intent = new Intent(CartCustomerActivity.this, OrderCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_cart){

        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(CartCustomerActivity.this);
        return sessionManager.getUserId();
    }
}
