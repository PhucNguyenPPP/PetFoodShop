package com.example.petshopproject.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.petshopproject.adapter.FoodManagementAdapter;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.FoodPet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodAdminActivity extends AppCompatActivity {

    AppDatabase mDb;
    ListView lvFoodManagement;
    List<Food> arrayFood;
    FoodManagementAdapter adapter;
    ImageView imageAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();

        lvFoodManagement = findViewById(R.id.lvFoodManagement);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        arrayFood = new ArrayList<>();
        adapter = new FoodManagementAdapter(this, R.layout.row_food_management, arrayFood);
        lvFoodManagement.setAdapter(adapter);

        GetDataFood();

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddFood();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home_admin) {
            Intent intent = new Intent(FoodAdminActivity.this, HomeAdminActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_food_admin) {

        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(FoodAdminActivity.this);
        return sessionManager.getUserId();
    }

    private void GetDataFood(){
        arrayFood.clear();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Food> foods = mDb.foodDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayFood.addAll(foods);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void DialogAddFood(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_food);

        EditText etFoodName = (EditText) dialog.findViewById(R.id.etFoodName);
        EditText etFoodDescription = (EditText) dialog.findViewById(R.id.etFoodDescription);
        EditText etPrice = (EditText) dialog.findViewById(R.id.etPrice);
        EditText etAmountInStock = (EditText) dialog.findViewById(R.id.etAmountInStock);
        EditText etImageLink = (EditText) dialog.findViewById(R.id.etImageLink);
        EditText etWeight = (EditText) dialog.findViewById(R.id.etWeight);
        CheckBox cbCat = (CheckBox) dialog.findViewById(R.id.cbCat);
        CheckBox cbDog = (CheckBox) dialog.findViewById(R.id.cbDog);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = etFoodName.getText().toString();
                String foodDescription = etFoodDescription.getText().toString();
                String price = etPrice.getText().toString();
                String amountInStock = etAmountInStock.getText().toString();
                String imageLink = etImageLink.getText().toString();
                String weight = etWeight.getText().toString();
                boolean isCatChecked = cbCat.isChecked();
                boolean isDogChecked = cbDog.isChecked();

                if(TextUtils.isEmpty(foodName)){
                    Toast.makeText(FoodAdminActivity.this, "Please input food name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(foodDescription)){
                    Toast.makeText(FoodAdminActivity.this, "Please input food description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(price)){
                    Toast.makeText(FoodAdminActivity.this, "Please input price", Toast.LENGTH_SHORT).show();
                    return;
                }

                float parsePrice = Float.parseFloat(price);
                if(parsePrice <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Price must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(amountInStock)){
                    Toast.makeText(FoodAdminActivity.this, "Please input amount in stock", Toast.LENGTH_SHORT).show();
                    return;
                }

                int parseAmountInStock = Integer.parseInt(amountInStock);
                if(parseAmountInStock <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Amount in stock must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(imageLink)){
                    Toast.makeText(FoodAdminActivity.this, "Please input image link", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(weight)){
                    Toast.makeText(FoodAdminActivity.this, "Please input weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                float parseWeight = Float.parseFloat(weight);
                if(parseWeight <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Weight must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isCatChecked && !isDogChecked){
                    Toast.makeText(FoodAdminActivity.this, "Please choose at least 1 pet", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        String foodId = UUID.randomUUID().toString();
                        mDb.foodDao().insert(new Food(foodId,
                                foodName,
                                imageLink,
                                foodDescription,
                                parsePrice,
                                parseWeight,
                                parseAmountInStock,
                                true));

                        if(isCatChecked){
                            String catId = mDb.petDao().getCat().getPetId();
                            mDb.foodPetDao().insert(new FoodPet(UUID.randomUUID().toString(),
                                    foodId, catId));
                        }

                        if(isDogChecked){
                            String catId = mDb.petDao().getDog().getPetId();
                            mDb.foodPetDao().insert(new FoodPet(UUID.randomUUID().toString(),
                                    foodId, catId));
                        }
                    }
                });
                Toast.makeText(FoodAdminActivity.this, "Add food successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataFood();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DialogUpdateFood(String foodId, String foodName, String foodDescription,
                                 String price, String amountInStock, String imageLink,
                                 String weight, boolean isForCat, boolean isForDog){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_food);

        EditText etFoodName = (EditText) dialog.findViewById(R.id.etFoodName);
        EditText etFoodDescription = (EditText) dialog.findViewById(R.id.etFoodDescription);
        EditText etPrice = (EditText) dialog.findViewById(R.id.etPrice);
        EditText etAmountInStock = (EditText) dialog.findViewById(R.id.etAmountInStock);
        EditText etImageLink = (EditText) dialog.findViewById(R.id.etImageLink);
        EditText etWeight = (EditText) dialog.findViewById(R.id.etWeight);
        CheckBox cbCat = (CheckBox) dialog.findViewById(R.id.cbCat);
        CheckBox cbDog = (CheckBox) dialog.findViewById(R.id.cbDog);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        etFoodName.setText(foodName);
        etFoodDescription.setText(foodDescription);
        etPrice.setText(price);
        etAmountInStock.setText(amountInStock);
        etImageLink.setText(imageLink);
        etWeight.setText(weight);
        if(isForCat){
            cbCat.setChecked(true);
        }

        if(isForDog){
            cbDog.setChecked(true);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String foodName = etFoodName.getText().toString();
                String foodDescription = etFoodDescription.getText().toString();
                String price = etPrice.getText().toString();
                String amountInStock = etAmountInStock.getText().toString();
                String imageLink = etImageLink.getText().toString();
                String weight = etWeight.getText().toString();
                boolean isCatChecked = cbCat.isChecked();
                boolean isDogChecked = cbDog.isChecked();

                if(TextUtils.isEmpty(foodName)){
                    Toast.makeText(FoodAdminActivity.this, "Please input food name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(foodDescription)){
                    Toast.makeText(FoodAdminActivity.this, "Please input food description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(price)){
                    Toast.makeText(FoodAdminActivity.this, "Please input price", Toast.LENGTH_SHORT).show();
                    return;
                }

                float parsePrice = Float.parseFloat(price);
                if(parsePrice <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Price must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(amountInStock)){
                    Toast.makeText(FoodAdminActivity.this, "Please input amount in stock", Toast.LENGTH_SHORT).show();
                    return;
                }

                int parseAmountInStock = Integer.parseInt(amountInStock);
                if(parseAmountInStock <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Amount in stock must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(imageLink)){
                    Toast.makeText(FoodAdminActivity.this, "Please input image link", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(weight)){
                    Toast.makeText(FoodAdminActivity.this, "Please input weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                float parseWeight = Float.parseFloat(weight);
                if(parseWeight <= 0) {
                    Toast.makeText(FoodAdminActivity.this, "Weight must be larger than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isCatChecked && !isDogChecked){
                    Toast.makeText(FoodAdminActivity.this, "Please choose at least 1 pet", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.foodDao().update(new Food(foodId,
                                foodName,
                                imageLink,
                                foodDescription,
                                parsePrice,
                                parseWeight,
                                parseAmountInStock,
                                true));

                        mDb.foodPetDao().deleteFoodPetByFoodId(foodId);

                        if(isCatChecked){
                            String catId = mDb.petDao().getCat().getPetId();
                            mDb.foodPetDao().insert(new FoodPet(UUID.randomUUID().toString(),
                                    foodId, catId));
                        }

                        if(isDogChecked){
                            String catId = mDb.petDao().getDog().getPetId();
                            mDb.foodPetDao().insert(new FoodPet(UUID.randomUUID().toString(),
                                    foodId, catId));
                        }
                    }
                });
                Toast.makeText(FoodAdminActivity.this, "Update food successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataFood();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DialogDeleteFood(String foodId, String foodName){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Do you want to delete " + foodName);
        dialogXoa.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.foodDao().delete(foodId);
                    }
                });
                Toast.makeText(FoodAdminActivity.this, "Delete food successfully", Toast.LENGTH_SHORT).show();
                GetDataFood();
            }
        });

        dialogXoa.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }
}
