package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopproject.R;

public class CheckOutActivity extends AppCompatActivity {

    String distance;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        Intent intent = getIntent();
        if (intent != null) {
            distance = intent.getStringExtra("Distance");
            address = intent.getStringExtra("Address");
            Toast.makeText(this,"Distance: " + distance + " Address: " + address,  Toast.LENGTH_LONG).show();
        }
    }
}
