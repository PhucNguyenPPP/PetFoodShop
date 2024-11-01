package com.example.petshopproject.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.adapter.AppDatabase;
import com.example.petshopproject.adapter.AppExecutors;
import com.example.petshopproject.adapter.SessionManager;
import com.example.petshopproject.model.Pet;
import com.example.petshopproject.model.Role;
import com.example.petshopproject.model.User;
import com.example.petshopproject.ui.admin.HomeAdminActivity;
import com.example.petshopproject.ui.customer.HomeCustomerActivity;
import com.example.petshopproject.ui.shipper.HomeShipperActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;


public class SignInActivity extends AppCompatActivity {

    TextView txtSignUp;
    Button btnSignIn;
    TextInputEditText txtUserName;
    TextInputEditText txtPassword;
    TextView textError;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        txtSignUp = (TextView) findViewById(R.id.signup_option);
        txtUserName = (TextInputEditText) findViewById(R.id.username);
        txtPassword = (TextInputEditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.login_button);
        textError = (TextView) findViewById(R.id.textError);

        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "PetShopDb")
                .build();
        // Mới lấy project này về thì check trong db bảng role vs pet có data chưa,
        // nếu chưa bỏ comment đoạn code này rồi run project 1 lần là dc
        // sau đó comment dòng code này lại
        // đừng có để dòng code này chạy 2 lần k thôi là lỗi á

//        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.roleDao().insert(new Role("c86bf5c9-cae7-48e0-a81d-0789862c713e","Customer"));
//                mDb.roleDao().insert(new Role("686bf125-98f6-44a0-8df5-21ee8821e7d7","Admin"));
//                mDb.roleDao().insert(new Role("80d1cff2-b293-4fac-b894-e0728b70c011","Shipper"));
//                mDb.petDao().insert(new Pet("225162e2-fbc6-408b-b489-afe4af785ede", "Cat"));
//                mDb.petDao().insert(new Pet("9f368269-7a91-4664-b307-b4ef18471e13", "Dog"));
//                mDb.userDao().insert(new User("cedf1286-61c4-4a51-a80e-5184f088b6c3", "admin123",
//                        "123", "Admin", "0987465464", "admin@gmail.com",
//                        true, "686bf125-98f6-44a0-8df5-21ee8821e7d7"));
//                mDb.userDao().insert(new User("0123c889-6486-4de3-9262-7110f5517f3f", "customer1",
//                        "123", "Customer1", "0698556987", "customer1@gmail.com", true,
//                        "c86bf5c9-cae7-48e0-a81d-0789862c713e"));
//                mDb.userDao().insert(new User("60b37682-cb67-484e-9d94-def4b392901c", "shipper1",
//                        "123", "Shipper1", "0256987968", "shipper1@gmail.com", true,
//                        "80d1cff2-b293-4fac-b894-e0728b70c011"));
//            }
//        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtUserName.getText().toString())) {
                    textError.setText("Please input username");
                    return;
                }

                if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    textError.setText("Please input password");
                    return;
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<User> user = mDb.userDao().getAll();
                        boolean checkLogin = mDb.userDao().checkLogin(
                                txtUserName.getText().toString()
                                , txtPassword.getText().toString());
                        if (!checkLogin) {
                            runOnUiThread(() -> textError.setText("Username or password incorrect"));
                        } else {
                            User userDb = mDb.userDao().getUserByUserName(txtUserName.getText().toString());

                            SessionManager sessionManagement = new SessionManager(SignInActivity.this);

                            String shipperRoleId = mDb.roleDao().getShipperRole().getRoleId();
                            String adminRoleId = mDb.roleDao().getAdminRole().getRoleId();
                            String customerRoleId = mDb.roleDao().getCustomerRole().getRoleId();

                            if(userDb.getRoleId().equals(customerRoleId)){
                                sessionManagement.createLoginSession(userDb.getUserId(), userDb.getUserName());
                                Intent intent = new Intent(SignInActivity.this, HomeCustomerActivity.class);
                                startActivity(intent);
                            } else if (userDb.getRoleId().equals(adminRoleId)) {
                                sessionManagement.createLoginSession(userDb.getUserId(), userDb.getUserName());
                                Intent intent = new Intent(SignInActivity.this, HomeAdminActivity.class);
                                startActivity(intent);
                            } else if (userDb.getRoleId().equals(shipperRoleId)) {
                                sessionManagement.createLoginSession(userDb.getUserId(), userDb.getUserName());
                                Intent intent = new Intent(SignInActivity.this, HomeShipperActivity.class);
                                startActivity(intent);
                            } else {
                                runOnUiThread(() -> textError.setText("Access denied"));
                            }
                        }
                    }
                });
            }
        });
    }
}
