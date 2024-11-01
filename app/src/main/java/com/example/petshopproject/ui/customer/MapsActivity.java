package com.example.petshopproject.ui.customer;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.petshopproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng fixedLocation = new LatLng(10.7769, 106.6953); // Vị trí cố định
    private LatLng userLocation;
    private Marker userMarker; // Marker cho vị trí người dùng
    float distanceInKm;
    String address;
    Button btnConfirmAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Khởi tạo map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent1 = getIntent();
        Double total = intent1.getDoubleExtra("price",0);
        btnConfirmAddress = (Button) findViewById(R.id.btnConfirmAddress);
        btnConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, CheckOutActivity.class);
                intent.putExtra("Address", address);
                intent.putExtra("Distance", String.valueOf(distanceInKm));
                intent.putExtra("price", total);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Đặt vị trí cố định trên bản đồ
        mMap.addMarker(new MarkerOptions().position(fixedLocation).title("Fixed Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 12));

        // Thêm sự kiện nhấp chuột vào bản đồ
        mMap.setOnMapClickListener(latLng -> {
            userLocation = latLng;

            // Xóa marker cũ nếu tồn tại
            if (userMarker != null) {
                userMarker.remove();
            }

            // Thêm marker mới cho vị trí người dùng
            userMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));

            // Tính khoảng cách và lấy địa chỉ
            calculateDistanceAndAddress(userLocation, fixedLocation);
        });
    }

    private void calculateDistanceAndAddress(LatLng userLocation, LatLng fixedLocation) {
        float[] results = new float[1];
        Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                fixedLocation.latitude, fixedLocation.longitude, results);
        distanceInKm = results[0] / 1000;

        // Lấy địa chỉ từ tọa độ
        address = getAddressFromLatLng(userLocation);

        // Hiển thị khoảng cách và địa chỉ
        String message = "Khoảng cách: " + distanceInKm + " km\nĐịa chỉ: " + address;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0); // Trả về địa chỉ đầu tiên
            } else {
                return "Không tìm thấy địa chỉ";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi khi tìm địa chỉ";
        }
    }
}
