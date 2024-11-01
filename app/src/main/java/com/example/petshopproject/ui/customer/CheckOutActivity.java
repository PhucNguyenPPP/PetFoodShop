package com.example.petshopproject.ui.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.CartCustomerAdapter;
import com.example.petshopproject.adapter.FoodCustomerAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Cart;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.OrderDetail;
import com.example.petshopproject.Api.CreateOrder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {

    String distance;
    String address;
    AppDatabase mDb;
    double total;
    double shipFee;
    private TextView tvProductPrice;
    private RadioButton rbCash, rbZalo;
    private Button btnAddress, btnCheckout;
    Button btnBack;
    TextView tvShip;
    TextView tvTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        tvShip = findViewById(R.id.txtPhiShip);
        tvTotal = findViewById(R.id.tv_totalPrice);
        ZaloPaySDK.init(554
                , Environment.SANDBOX);
        Intent intent = getIntent();
        if (intent != null) {
            total = intent.getDoubleExtra("price",0);
            distance = intent.getStringExtra("Distance");
            address = intent.getStringExtra("Address");
            if(distance!=null && address !=null)
            {
                shipFee = Double.parseDouble(distance)*(13000);
                tvShip.setText("Shipping fee: "+String.valueOf(shipFee));
                if(total>0)
                {
                    tvTotal.setText(String.valueOf("Total: "+shipFee+total));
                }

            }
            Toast.makeText(this, "Distance: " + distance + " Address: " + address + " Total: " + total, Toast.LENGTH_LONG).show();
        }
        tvProductPrice = findViewById(R.id.tv_productPrice);
        btnBack = findViewById(R.id.btnBack);
        tvProductPrice.setText("Price of products: " + String.valueOf(total));
        rbCash = findViewById(R.id.rb_cash);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
        rbZalo = findViewById(R.id.rb_Zalo);
        btnAddress = findViewById(R.id.btn_address);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(distance==null||address==null){
                    Toast.makeText(CheckOutActivity.this, "Please choose your address", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!rbCash.isChecked()&&!rbZalo.isChecked())
                {
                    Toast.makeText(CheckOutActivity.this, "Please choose your payment method", Toast.LENGTH_LONG).show();
                    return;
                }
                if(rbCash.isChecked())
                {
                    AddDbOrderCash();
                    Toast.makeText(CheckOutActivity.this, "Order Sucessfully!", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(CheckOutActivity.this,HomeCustomerActivity.class);
                    startActivity(intent1);
                    return;
                }
                if(rbZalo.isChecked())
                {
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        double result = shipFee + total;
                        String totalString = String.format("%.0f", result);
                        JSONObject data = orderApi.createOrder(totalString);
                        String code = data.getString("returncode");
                        if (code.equals("1")) {
                            String token = data.getString("zptranstoken");
                            ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {

                                    Log.d("ZaloPay", "ZaloPay SDK Succeeded");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(CheckOutActivity.this)
                                                    .setTitle("Payment Success")
                                                    .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(CheckOutActivity.this, HomeCustomerActivity.class);
                                                            Toast.makeText(CheckOutActivity.this, "Pay sucessfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", null).show();
                                        }
                                    });
                                    AddDbOrderZalopay();
                                }

                                @Override
                                public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                    new AlertDialog.Builder(CheckOutActivity.this)
                                            .setTitle("User Cancel Payment")
                                            .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent1 = new Intent(CheckOutActivity.this, CheckOutActivity.class);
                                                    startActivity(intent1);

                                                    Log.d("ZaloPay", "ZaloPay SDK Canceled");

                                                }
                                            })
                                            .setNegativeButton("Cancel", null).show();
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                    new AlertDialog.Builder(CheckOutActivity.this)
                                            .setTitle("Payment Fail")
                                            .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.d("ZaloPay", "ZaloPay SDK Error");
                                                }
                                            })
                                            .setNegativeButton("Cancel", null).show();
                                }
                            });
                        }

                    } catch (Exception e) {
                        Log.d("ZaloPay", "ZaloPay SDK Exception");
                        e.printStackTrace();
                    }
                }

            }
        });
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CheckOutActivity.this,MapsActivity.class);
                intent1.putExtra("price",total);
                startActivity(intent1);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CheckOutActivity.this,CartCustomerActivity.class);
                startActivity(intent1);
            }
        });

    }

    private void AddDbOrderCash() {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            String orderId = UUID.randomUUID().toString();
            Random random = new Random();
            String orderNumber = "O" + String.valueOf(random.nextInt());
            Order order = new Order(orderId, orderNumber, (float) total, (float) shipFee, address, "Unpaid", getCurrentUserId(), null); //tại Phúc để non null nên t sợ lỗi
            mDb.orderDao().insert(order);
            List<Cart> carts = mDb.cartDao().getCartByUser(getCurrentUserId());
            for (Cart cart: carts) {
                String orderDetailId = UUID.randomUUID().toString();
                Food food = mDb.foodDao().getFoodById(cart.getFoodId());
                OrderDetail orderDetail = new OrderDetail(orderDetailId, orderId,cart.getFoodId(),cart.getAmount(),food.getPrice()*cart.getAmount());
                mDb.orderDetailDao().insert(orderDetail);
                food.setAmountInStock(food.getAmountInStock()-cart.getAmount());
                mDb.foodDao().update(food);
                mDb.cartDao().delete(cart);
            }
        });

    }

    private void AddDbOrderZalopay() {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            String orderId = UUID.randomUUID().toString();
            Random random = new Random();
            String orderNumber = "O" + String.valueOf(random.nextInt());
            Order order = new Order(orderId, orderNumber, (float) total, (float) shipFee, address, "Paid", getCurrentUserId(), null); //tại Phúc để non null nên t sợ lỗi
            mDb.orderDao().insert(order);
            List<Cart> carts = mDb.cartDao().getCartByUser(getCurrentUserId());
            for (Cart cart: carts) {
                String orderDetailId = UUID.randomUUID().toString();
                Food food = mDb.foodDao().getFoodById(cart.getFoodId());
                OrderDetail orderDetail = new OrderDetail(orderDetailId, orderId,cart.getFoodId(),cart.getAmount(),food.getPrice()*cart.getAmount());
                mDb.orderDetailDao().insert(orderDetail);
                food.setAmountInStock(food.getAmountInStock()-cart.getAmount());
                mDb.foodDao().update(food);
                mDb.cartDao().delete(cart);
            }
        });

    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(CheckOutActivity.this);
        return sessionManager.getUserId();
    }
}
