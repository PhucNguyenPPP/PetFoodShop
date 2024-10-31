package com.example.petshopproject.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.CustomerOrderAdapter;
import com.example.petshopproject.adapter.HomeAdminAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.ui.Order.OrderDetailActivity;
import com.example.petshopproject.ui.customer.OrderCustomerActivity;
import com.example.petshopproject.ui.shipper.HomeShipperActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminActivity extends AppCompatActivity {

    ListView lvOrder;
    HomeAdminAdapter adapter;
    List<Order> orderArray;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();

        orderArray = new ArrayList<>();
        adapter = new HomeAdminAdapter(this, R.layout.item_order_history, orderArray);
        lvOrder = findViewById(R.id.AdminListView);

        lvOrder.setAdapter(adapter);
        GetDataOrder();

        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order selectedOrder = orderArray.get(position);
                if (selectedOrder != null) {
                    String orderId = selectedOrder.getOrderId();
                    Log.d("OrderDetail", "Selected Order ID: " + orderId); // Thêm log để kiểm tra
                    Intent intent = new Intent(HomeAdminActivity.this, OrderDetailActivity.class);
                    intent.putExtra("ORDER_ID", orderId);
                    startActivity(intent);
                } else {
                    Log.e("OrderDetail", "Selected order is null");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_admin){

        } else if (item.getItemId() == R.id.navigation_food_admin){
            Intent intent = new Intent(HomeAdminActivity.this, FoodAdminActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(HomeAdminActivity.this);
        return sessionManager.getUserId();
    }

    private void GetDataOrder(){
        orderArray.clear();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Order>orders = mDb.orderDao().getAll();
                if(orders.stream().count() == 0){

                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderArray.addAll(orders);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
