package com.example.petshopproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "customerId"
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "shipperId"
                )
        }
)
public class Order {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "orderId")
    private String orderId;

    @NonNull
    @ColumnInfo(name = "orderNumber")
    private String orderNumber;

    @NonNull
    @ColumnInfo(name = "totalPrice")
    private float totalPrice;

    @NonNull
    @ColumnInfo(name = "shippingFee")
    private float shippingFee;

    @NonNull
    @ColumnInfo(name = "address")
    private String address;

    @NonNull
    @ColumnInfo(name = "status")
    private String status;

    @NonNull
    @ColumnInfo(name = "customerId")
    private String customerId;

    @NonNull
    @ColumnInfo(name = "shipperId")
    private String shipperId;

    public Order() {
    }

    public Order(@NonNull String orderId, @NonNull String orderNumber, float totalPrice, float shippingFee, @NonNull String address, @NonNull String status, @NonNull String customerId, @NonNull String shipperId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.shippingFee = shippingFee;
        this.address = address;
        this.status = status;
        this.customerId = customerId;
        this.shipperId = shipperId;
    }

    @NonNull
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {
        this.orderId = orderId;
    }

    @NonNull
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(@NonNull String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(float shippingFee) {
        this.shippingFee = shippingFee;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(@NonNull String customerId) {
        this.customerId = customerId;
    }

    @NonNull
    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(@NonNull String shipperId) {
        this.shipperId = shipperId;
    }
}
