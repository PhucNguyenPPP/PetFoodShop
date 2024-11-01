package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.petshopproject.adapter.CartCustomerAdapter;
import com.example.petshopproject.adapter.FoodCustomerAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Cart;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.ui.auth.SignInActivity;

import java.util.ArrayList;
import java.util.List;

public class CartCustomerActivity extends AppCompatActivity {
    AppDatabase mDb;
    ListView lvCart;
    List<Cart> arrayCart;
    CartCustomerAdapter adapter;
    Button btnCheckout;
    List<Cart> cart;
    int stockQuantity;

    Double total = (double) 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();

        lvCart = findViewById(R.id.listViewCart);
        btnCheckout = findViewById(R.id.buttonCheckout);
        arrayCart = new ArrayList<>();
        adapter = new CartCustomerAdapter(CartCustomerActivity.this, arrayCart, R.layout.row_cart_customer);
        lvCart.setAdapter(adapter);
        GetDataCart();
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin giỏ hàng của người dùng hiện tại
    //            List<Cart> cart = getCartByUserId(getCurrentUserId());

                // Kiểm tra số lượng tồn kho của từng sản phẩm
                boolean isAllProductInStock = true;
                for (Cart item : arrayCart) {
                    int stockQuantity = getStockQuantity(item.getFoodId());
                    if (item.getAmount() > stockQuantity) {
                        isAllProductInStock = false;
                        // Hiển thị thông báo lỗi
                        break;
                    }
                }
                if (isAllProductInStock) {
                    // Tạo đơn hàng mới

                    // Chuyển hướng
                    Intent intent = new Intent(CartCustomerActivity.this, CheckOutActivity.class);
                    intent.putExtra("userId", getCurrentUserId());
                    intent.putExtra("price",total);
                    startActivity(intent);
                } else {
                    runOnUiThread(() -> Toast.makeText(CartCustomerActivity.this, "Invalid quantity. Please enter a positive value.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private int getStockQuantity(String foodId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                stockQuantity = mDb.foodDao().getFoodById(foodId).getAmountInStock();
            }
        });
        return stockQuantity;
    }

    private List<Cart> getCartByUserId(String userid)
    {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                cart= mDb.cartDao().

                        getCartByUser(userid);
            }
        });
        return cart;
    }


    private void GetDataCart() {
        total = (double)0;
        arrayCart.clear();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Cart> carts = (List<Cart>) mDb.cartDao().getCartByUser(getCurrentUserId());
                arrayCart.addAll(carts);
                List<Food> foodDetails = new ArrayList<>();
                for (Cart cart : carts) {
                    String foodId = cart.getFoodId();
                    Food food = mDb.foodDao().getFoodById(foodId);
                    if (food != null) {
                        foodDetails.add(food);
                        double temp =food.getPrice() * cart.getAmount();
                        total = total + temp;
                    }
                }
                adapter.updateItems(carts, foodDetails);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home_customer) {
            Intent intent = new Intent(CartCustomerActivity.this, HomeCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_order_customer) {
            Intent intent = new Intent(CartCustomerActivity.this, OrderCustomerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_cart) {

        } else if (item.getItemId() == R.id.sign_out_btn) {
            SessionManager sessionManagement = new SessionManager(CartCustomerActivity.this);
            sessionManagement.logout();
            Intent intent = new Intent(CartCustomerActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(CartCustomerActivity.this);
        return sessionManager.getUserId();
    }

    public void UpdateQuantity(String foodId, String userId, int availableAmount, int newQuantity) {
        if (newQuantity <= 0) {
            // Handle invalid quantity (show a toast or message)
            runOnUiThread(() -> Toast.makeText(CartCustomerActivity.this, "Invalid quantity. Please enter a positive value.", Toast.LENGTH_SHORT).show());
            return;
        }

        if (newQuantity > availableAmount) {
            // Handle insufficient stock (show a toast or message)
            runOnUiThread(() -> Toast.makeText(CartCustomerActivity.this, "Insufficient stock. Only " + availableAmount + " items available.", Toast.LENGTH_SHORT).show());
            adapter.notifyDataSetChanged();
            GetDataCart();
            return;
        }

        // Update cart quantity in background thread
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Update cart quantity
                Cart cart = mDb.cartDao().getCartByFoodAndUser(foodId, userId);
                cart.setAmount(newQuantity);
                mDb.cartDao().update(cart);
                // Update UI in main thread (if necessary
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        GetDataCart();
                        Toast.makeText(CartCustomerActivity.this, "Update quantity sucessfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    public void DeleteItem(String cartId) {
        List<Cart> cart = mDb.cartDao().getCartByCartId(cartId);
        for (Cart cart1: cart) {
            mDb.cartDao().delete(cart1);
        }// Update UI in main thread (if necessary
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                GetDataCart();
                Toast.makeText(CartCustomerActivity.this, "Delete quantity sucessfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Custom method to update cart quantity in local data (optional)

}
