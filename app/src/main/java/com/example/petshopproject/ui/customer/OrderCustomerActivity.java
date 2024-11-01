package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.CustomerOrderAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.User;
import com.example.petshopproject.ui.Order.OrderDetailActivity;
import com.example.petshopproject.ui.auth.SignInActivity;
import com.example.petshopproject.ui.auth.SignUpActivity;
import com.example.petshopproject.ui.shipper.HomeShipperActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderCustomerActivity extends AppCompatActivity {

    ListView lvOrder;
    CustomerOrderAdapter adapter;
    List<Order> orderArray;
    AppDatabase mDb;
    TextView noti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();

        orderArray = new ArrayList<>();
        adapter = new CustomerOrderAdapter(this, R.layout.item_order_history_customer, orderArray);
        lvOrder = findViewById(R.id.lvOrder);
        noti = findViewById(R.id.textViewNoti);


        lvOrder.setAdapter(adapter);
        GetDataOrder();

        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order selectedOrder = orderArray.get(position);
                if (selectedOrder != null) {
                    String orderId = selectedOrder.getOrderId();
                    Log.d("OrderDetail", "Selected Order ID: " + orderId); // Thêm log để kiểm tra
                    Intent intent = new Intent(OrderCustomerActivity.this, OrderDetailActivity.class);
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
        getMenuInflater().inflate(R.menu.customer_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_customer){
            Intent intent = new Intent(OrderCustomerActivity.this, HomeCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_order_customer){

        } else if (item.getItemId() == R.id.navigation_cart){
            Intent intent = new Intent(OrderCustomerActivity.this, CartCustomerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                //getCurrentUserId();
                List<Order> order1 = new ArrayList<>();
                for(int i = 0; i< orders.size(); i++){
                    if(orders.get(i).getCustomerId().equals(getCurrentUserId())){
                        order1.add(orders.get(i));
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderArray.addAll(order1);
                        if(order1.stream().count() == 0)
                            noti.setText("You have no order!");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(OrderCustomerActivity.this);
        return sessionManager.getUserId();
    }

    private void AddUser() {
        User newUser1 = new User("4", "Admin", "1", "Admin ne", "123456", "admin@gmail.com", true, "3");

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.userDao().insert(newUser1); // Thêm vào cơ sở dữ liệu
            }
        });
    }
}
