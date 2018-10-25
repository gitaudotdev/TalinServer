package gitau.dev.talinserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gitau.dev.talinserver.Models.Order;
import gitau.dev.talinserver.R;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name,quantity,price,discount;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);
        discount = itemView.findViewById(R.id.product_discount);

    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOrders;
//    Context mContext;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
//        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_detail_layout,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name: %s",order.getProductName()));
        holder.quantity.setText(String.format("Quantity: %s",order.getQuantity()));
        holder.price.setText(String.format("Price: %s",order.getPrice()));
        holder.discount.setText(String.format("Discount: %s",order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
