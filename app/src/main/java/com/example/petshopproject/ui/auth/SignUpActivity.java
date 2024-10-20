package com.example.petshopproject.ui.auth;

import android.annotation.SuppressLint;
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
import com.example.petshopproject.model.Role;
import com.example.petshopproject.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    TextView textSignIn;
    TextView textError;
    Button btnSignUp;
    TextInputEditText txtFullName;
    TextInputEditText txtUsername;
    TextInputEditText txtPassword;
    TextInputEditText txtPhone;
    TextInputEditText txtEmail;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        textSignIn = (TextView) findViewById(R.id.signin_option);
        textError = (TextView) findViewById(R.id.textError);
        btnSignUp = (Button) findViewById(R.id.signup_button);
        txtFullName = (TextInputEditText) findViewById(R.id.fullname);
        txtUsername = (TextInputEditText) findViewById(R.id.username);
        txtPassword = (TextInputEditText) findViewById(R.id.password);
        txtPhone = (TextInputEditText) findViewById(R.id.phone);
        txtEmail = (TextInputEditText) findViewById(R.id.email);

        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "PetShopDb").build();

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String fullName = txtFullName.getText().toString();
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String phone = txtPhone.getText().toString();
                String email = txtEmail.getText().toString();

                if (TextUtils.isEmpty(fullName)) {
                    textError.setText("Please input full name");
                    return;
                }

                if (TextUtils.isEmpty(userName)) {
                    textError.setText("Please input username");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    textError.setText("Please input password");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    textError.setText("Please input phone");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    textError.setText("Please input email");
                    return;
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean userExists = mDb.userDao().userExist(userName);
                        if (userExists) {
                            runOnUiThread(() -> textError.setText("Username already exists"));
                            return;
                        }

                        boolean phoneExists = mDb.userDao().phoneExist(phone);
                        if (phoneExists) {
                            runOnUiThread(() -> textError.setText("Phone already exists"));
                            return;
                        }

                        boolean emailExists = mDb.userDao().emailExist(email);
                        if (emailExists) {
                            runOnUiThread(() -> textError.setText("Email already exists"));
                            return;
                        }

                        Role customerRole = mDb.roleDao().getCustomerRole();
                        if(customerRole == null){
                            runOnUiThread(() -> textError.setText("Customer role is null"));
                            return;
                        }
                            User user = new User(UUID.randomUUID().toString(),
                                    userName,
                                    password,
                                    fullName,
                                    phone,
                                    email,
                                    true,
                                    customerRole.getRoleId());

                            mDb.userDao().insert(user);
                            finish();
                    }
                });
            }
        });
    }
}
