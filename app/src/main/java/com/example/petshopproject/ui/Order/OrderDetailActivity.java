package com.example.petshopproject.ui.Order;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.OrderDetailAdapter;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    AppDatabase mDb;
    OrderDetailAdapter adapter;
    List<OrderDetail> orderDetail;
    List<Food> foodList;
    TextView txtOrderId;
    TextView txtAmount;
    TextView txtPrice;
    ListView lvFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
        txtOrderId = (TextView) findViewById(R.id.txtOrderId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        lvFood = findViewById(R.id.lvFood);

        foodList = new ArrayList<>();
        orderDetail = new ArrayList<>();

        String orderId = getIntent().getStringExtra("ORDER_ID");
        int orderId2 = Integer.parseInt(orderId);

        //lvFood.setAdapter(adapter);
        GetOrderDetail(orderId2);
        //adapter = new OrderDetailAdapter(this, R.layout.row_order_detail, foodList, orderDetail, orderId);
        //lvFood = (ListView) findViewById(R.id.lvFood);



    }

    private void GetOrderDetail(int orderId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                orderDetail = mDb.orderDetailDao().getOrderDetailsByOrderId(orderId); // Phương thức lấy OrderDetail
                Order order = mDb.orderDao().getOrderById(String.valueOf(orderId));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (orderDetail != null) {
                            txtOrderId.setText(String.valueOf(orderId));
                            txtPrice.setText(String.valueOf(order.getTotalPrice() + order.getShippingFee()));// Hiển thị thông tin chi tiết
                            GetDataOrder();
                        } else {
                            txtOrderId.setText("Không có chi tiết cho đơn hàng này.");
                        }
                    }
                });
            }
        });
    }
    private void GetDataOrder(){
        foodList.clear();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Food>foods = mDb.foodDao().getAll();
                if (foods.isEmpty() || orderDetail.isEmpty()) { // Kiểm tra nếu không có dữ liệu
                    runOnUiThread(() -> txtAmount.setText("Không có chi tiết đơn hàng"));
                    return;
                }
                int amount = 0;

                List<Food> Food2 = new ArrayList<>();
                for (int i = 0; i <orderDetail.size() ; i++) {
                    for(int j = 0; j < foods.size(); j++){
                        if(foods.get(j).getFoodId().equals(orderDetail.get(i).getFoodId())){
                            Food2.add(foods.get(j));
                            amount += orderDetail.get(i).getAmount();
                            txtAmount.setText(String.valueOf(amount));
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foodList.addAll(Food2);
                        String orderId = getIntent().getStringExtra("ORDER_ID");
                        adapter = new OrderDetailAdapter(OrderDetailActivity.this, R.layout.row_order_detail, foodList, orderDetail, String.valueOf(orderId));
                        lvFood.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
