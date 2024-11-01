package com.example.petshopproject.ui.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.CustomerOrderAdapter;
import com.example.petshopproject.adapter.FoodCustomerAdapter;
import com.example.petshopproject.adapter.HomeShipperAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.dao.OrderDetailDao;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.OrderDetail;
import com.example.petshopproject.ui.Order.OrderDetailActivity;
import com.example.petshopproject.ui.auth.SignInActivity;
import com.example.petshopproject.ui.customer.CartCustomerActivity;
import com.example.petshopproject.ui.customer.HomeCustomerActivity;
import com.example.petshopproject.ui.customer.OrderCustomerActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeShipperActivity extends AppCompatActivity {

    AppDatabase mDb;
    ListView lvOrder;
    HomeShipperAdapter adapter;
    List<Order> orderArray;
    TextView noti;

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

        orderArray = new ArrayList<>();
        adapter = new HomeShipperAdapter(this, R.layout.item_order_history_shipper, orderArray);
        lvOrder = findViewById(R.id.lvShipperOrder);
        noti = findViewById(R.id.textViewNoti2);
        //AddUser();
        lvOrder.setAdapter(adapter);
        GetDataOrder();

        OrderDetailDao orderDetailDao = mDb.orderDetailDao();
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order selectedOrder = orderArray.get(position);
                if (selectedOrder != null) {
                    String orderId = selectedOrder.getOrderId();
                    Log.d("OrderDetail", "Selected Order ID: " + orderId); // Thêm log để kiểm tra
                    Intent intent = new Intent(HomeShipperActivity.this, OrderDetailActivity.class);
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
        getMenuInflater().inflate(R.menu.shipper_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.navigation_home_shipper){

        } else if (item.getItemId() == R.id.sign_out_btn) {
            SessionManager sessionManagement = new SessionManager(HomeShipperActivity.this);
            sessionManagement.logout();
            Intent intent = new Intent(HomeShipperActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderArray.addAll(orders);
                        if(orders.stream().count() == 0)
                            noti.setText("Bạn chưa có đơn hàng nào!");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(HomeShipperActivity.this);
        return sessionManager.getUserId();
    }
    private void AddUser() {
        OrderDetail orderDetail = new OrderDetail("3", "2", "2", 2, 500000);
        //OrderDetail orderDetail1  = new OrderDetail("1", "1", "1", "Admin ne", "123456", "admin@gmail.com", true, "3");

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.orderDetailDao().insert(orderDetail);
                adapter.notifyDataSetChanged();// Thêm vào cơ sở dữ liệu
            }
        });
    }
}
