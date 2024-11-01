package com.example.petshopproject.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.petshopproject.R;
import com.example.petshopproject.model.Cart;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.ui.customer.CartCustomerActivity;
import com.example.petshopproject.ui.customer.HomeCustomerActivity;

import java.util.List;

public class CartCustomerAdapter extends BaseAdapter {
    private CartCustomerActivity context;
    private int layout;
    private List<Cart> cartList;
    private List<Food> foodList;
    private AppDatabase mDb;

    public CartCustomerAdapter(CartCustomerActivity context, List<Cart> cartList, int layout) {
        this.context = context;
        this.cartList = cartList;
        this.layout = layout;
        this.mDb = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public CartCustomerActivity getContext() {
        return context;
    }

    public void setContext(CartCustomerActivity context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public AppDatabase getmDb() {
        return mDb;
    }

    public void setmDb(AppDatabase mDb) {
        this.mDb = mDb;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null,
            true);
        }
        TextView tvFoodName = convertView.findViewById(R.id.textFoodName);
        TextView tvAmountInStock = convertView.findViewById(R.id.textAmountInStock);
        EditText etQuantity = convertView.findViewById(R.id.editTextQuantity);
        TextView tvPrice = convertView.findViewById(R.id.textPrice);
        TextView tvFoodDescription  = convertView.findViewById(R.id.textFoodDescription);
        Button buttonUpdateQuantity = convertView.findViewById(R.id.btnUpdateQuantity);
        ImageView imageView = convertView.findViewById(R.id.imageFood);

        // ... các TextView khác để hiển thị các thông tin khác
        Button buttonDelete = convertView.findViewById(R.id.btnDelete);
        Cart cart = cartList.get(position);
        Food food = foodList.get(position);

        tvFoodDescription.setText(food.getFoodName());
        tvAmountInStock.setText(String.valueOf(food.getAmountInStock()));
        etQuantity.setText(String.valueOf(cart.getAmount()));
        tvPrice.setText(String.valueOf(food.getPrice()*cart.getAmount()));
        tvFoodDescription.setText(String.valueOf(food.getDescription()));
        Glide.with(context)
                .load(food.getImageLink())
                .into(imageView);
        // ... gán giá trị cho các TextView khác
        buttonUpdateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet code de add to cart
                SessionManager sessionManager = new SessionManager(context);
                String userId = sessionManager.getUserId();
                String foodId = food.getFoodId();
                int newQuantity = Integer.parseInt(String.valueOf(etQuantity.getText()));
                int availableAmount = food.getAmountInStock();
                context.UpdateQuantity(foodId,userId,availableAmount,newQuantity);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DeleteItem(cart.getCartId());
            }
        });
        return convertView;
    }
    public void updateItems(List<Cart> carts, List<Food> foods) {
        this.cartList = carts;
        this.foodList = foods;
        notifyDataSetChanged();
    }
}
