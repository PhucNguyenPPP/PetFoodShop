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
import com.example.petshopproject.model.User;
import com.example.petshopproject.ui.customer.OrderCustomerActivity;
import com.example.petshopproject.ui.shipper.HomeShipperActivity;

import java.util.List;

public class HomeShipperAdapter extends BaseAdapter {
    private HomeShipperActivity context;
    private int layout;
    private List<Order> orders;

    private AppDatabase mDb;

    public HomeShipperAdapter(HomeShipperActivity context, int layout, List<Order> orders) {
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
        TextView tvCustomerName;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.tvOrderId = (TextView) view.findViewById(R.id.tvOrderId2);
            holder.tvOrderNumber = (TextView) view.findViewById(R.id.tvOrderNumber2);
            holder.tvOrderAddress = (TextView) view.findViewById(R.id.tvOrderAddress2);
            holder.tvOrderTotal = (TextView) view.findViewById(R.id.tvOrderTotal2);
            holder.tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus2);
            holder.tvOrderShippingPrice = (TextView) view.findViewById(R.id.tvOrderShippingPrice2);
            holder.tvCustomerName = (TextView) view.findViewById(R.id.txtCustomerName2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Order order = orders.get(position);
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrderNumber.setText(order.getOrderNumber());
        holder.tvOrderAddress.setText(order.getAddress());
        holder.tvOrderTotal.setText((int)order.getTotalPrice() + " vnđ");
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderShippingPrice.setText((int) order.getShippingFee() + " vnđ");
        holder.tvCustomerName.setText("Loading...");

        AppExecutors.getInstance().getDiskIO().execute(() -> {
            UserDao userDao = mDb.userDao();
            String customerName = userDao.getFullNameByUserId(order.getCustomerId());

            ((Activity) context).runOnUiThread(() -> {
                holder.tvCustomerName.setText(customerName);
            });
        });
        return view;
    }
}
