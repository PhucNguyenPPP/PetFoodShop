package com.example.petshopproject.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.petshopproject.R;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.FoodPet;
import com.example.petshopproject.model.Pet;
import com.example.petshopproject.ui.admin.FoodAdminActivity;
import com.example.petshopproject.ui.customer.HomeCustomerActivity;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FoodCustomerAdapter extends BaseAdapter {

    private HomeCustomerActivity context;
    private int layout;
    private List<Food> foodList;

    private AppDatabase mDb;

    public FoodCustomerAdapter(HomeCustomerActivity context, int layout, List<Food> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
        this.mDb = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgFood;
        TextView textFoodName;
        TextView textDescription;
        TextView textAmountInStock;
        TextView textPrice;
        TextView textPet;
        TextView textWeight;
        Button btnAddToCart;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.textFoodName = (TextView) view.findViewById(R.id.textFoodName);
            holder.textDescription = (TextView) view.findViewById(R.id.textFoodDescription);
            holder.textAmountInStock = (TextView) view.findViewById(R.id.textAmountInStock);
            holder.textPrice = (TextView) view.findViewById(R.id.textPrice);
            holder.textPet = (TextView) view.findViewById(R.id.textPet);
            holder.textWeight = (TextView) view.findViewById(R.id.textWeight);
            holder.imgFood = (ImageView) view.findViewById(R.id.imageFood);
            holder.btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Food food = foodList.get(position);
        holder.textFoodName.setText(food.getFoodName());
        holder.textDescription.setText(food.getDescription());
        holder.textPrice.setText((int) food.getPrice() + " d");
        holder.textAmountInStock.setText("" + food.getAmountInStock());
        holder.textWeight.setText(food.getWeight() + " kg");

        Glide.with(context)
                .load(food.getImageLink())
                .into(holder.imgFood);

        AppExecutors.getInstance().getDiskIO().execute(() -> {
            List<String> petNames = getAssociatedPetNames(food.getFoodId());
            String pets = TextUtils.join(", ", petNames);

            context.runOnUiThread(() -> holder.textPet.setText(pets));
        });

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet code de add to cart
            }
        });

        return view;
    }

    private List<String> getAssociatedPetNames(String foodId) {
        List<FoodPet> foodPetList = mDb.foodPetDao().getFoodPetsForFood(foodId);
        List<String> petNames = new ArrayList<>();

        for (FoodPet foodPet : foodPetList) {
            Pet pet = mDb.petDao().getPetById(foodPet.getPetId());
            if (pet != null) {
                petNames.add(pet.getPetName());
            }
        }

        return petNames;
    }
}
