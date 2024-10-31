package com.example.petshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.petshopproject.R;
import com.example.petshopproject.model.Food;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.model.OrderDetail;
import com.example.petshopproject.ui.Order.OrderDetailActivity;
import com.example.petshopproject.ui.shipper.HomeShipperActivity;

import java.util.List;

public class OrderDetailAdapter extends BaseAdapter {
    private OrderDetailActivity context;
    private int layout;
    private List<Food> foodList;
    private List<OrderDetail> orderDetailList;
    private String orderId;
    private AppDatabase mDb;

    public OrderDetailAdapter(OrderDetailActivity context, int layout, List<Food> foodList, List<OrderDetail> orderDetailList, String orderId) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
        this.orderDetailList = orderDetailList;
        this.orderId = orderId;
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
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        TextView txtFoodName;
        TextView tvFoodPrice;
        TextView tvWeight;
        TextView tvDescription;
        TextView tvAmount;
        ImageView imageFood2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtFoodName = (TextView) view.findViewById(R.id.txtFoodName);
            holder.tvFoodPrice = (TextView) view.findViewById(R.id.txtFoodPrice);
            holder.tvWeight = (TextView) view.findViewById(R.id.txtFoodWeight);
            holder.tvDescription = (TextView) view.findViewById(R.id.txtFoodDescription);
            holder.tvAmount = (TextView) view.findViewById(R.id.txtAmountFood);
            holder.imageFood2 = (ImageView) view.findViewById(R.id.imageFood2);
            view.setTag(holder);
        } else {
            holder = (OrderDetailAdapter.ViewHolder) view.getTag();
        }

        Food food = foodList.get(position);
        holder.txtFoodName.setText(food.getFoodName());
        holder.tvFoodPrice.setText((int) food.getPrice() + " vnđ");
        holder.tvWeight.setText(String.valueOf(food.getWeight()) + " kg");
        holder.tvDescription.setText(food.getDescription());
        for(OrderDetail orderDetail : orderDetailList){
            if(orderDetail.getFoodId().equals(food.getFoodId()) && orderDetail.getOrderId().equals(orderId) ){
                holder.tvAmount.setText(String.valueOf(orderDetail.getAmount()));
                break;
            }
        }



        Glide.with(context)
                .load(food.getImageLink())
                .into(holder.imageFood2);
        return view;
    }
}
