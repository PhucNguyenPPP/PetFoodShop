package com.example.petshopproject.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.room.Room;

import com.example.petshopproject.R;
import com.example.petshopproject.dao.UserDao;
import com.example.petshopproject.model.Order;
import com.example.petshopproject.ui.customer.OrderCustomerActivity;

import java.util.List;
public class CustomerOrderAdapter extends BaseAdapter {

    private OrderCustomerActivity context;
    private int layout;
    private List<Order> orders;

    private AppDatabase mDb;

    public CustomerOrderAdapter(OrderCustomerActivity context, int layout, List<Order> orders) {
        this.context = context;
        this.layout = layout;
        this.orders = orders;
        this.mDb = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "PetShopDb")
                .build();
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public class ViewHolder {
        TextView tvOrderId;
        TextView tvOrderNumber;
        TextView tvOrderTotal;
        TextView tvOrderShippingPrice;
        TextView tvOrderAddress;
        TextView tvOrderStatus;
        TextView tvShipperName;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

       ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.tvOrderId = (TextView) view.findViewById(R.id.tvOrderId1);
            holder.tvOrderNumber = (TextView) view.findViewById(R.id.tvOrderNumber1);
            holder.tvOrderAddress = (TextView) view.findViewById(R.id.tvOrderAddress1);
            holder.tvOrderTotal = (TextView) view.findViewById(R.id.tvOrderTotal1);
            holder.tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus1);
            holder.tvOrderShippingPrice = (TextView) view.findViewById(R.id.tvOrderShippingPrice1);
            holder.tvShipperName = (TextView) view.findViewById(R.id.txtShipperName1);
            view.setTag(holder);
        } else {
            holder = (CustomerOrderAdapter.ViewHolder) view.getTag();
        }

        Order order = orders.get(position);
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrderNumber.setText(order.getOrderNumber());
        holder.tvOrderAddress.setText(order.getAddress());
        holder.tvOrderTotal.setText((int)order.getTotalPrice() + " vnđ");
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderShippingPrice.setText((int) order.getShippingFee() + " vnđ");
        holder.tvShipperName.setText("Loading...");

        AppExecutors.getInstance().getDiskIO().execute(() -> {
            UserDao userDao = mDb.userDao();
            String shipperName = userDao.getFullNameByUserId(order.getShipperId());

            ((Activity) context).runOnUiThread(() -> {
                holder.tvShipperName.setText(shipperName);
            });
        });
        return view;
    }

}
