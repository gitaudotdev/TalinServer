package gitau.dev.talinserver.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gitau.dev.talinserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder  {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    public Button btnEdit,btnRemove,btnDetail,btnDirection;



    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);

        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDetail = itemView.findViewById(R.id.btnDetail);
        btnDirection = itemView.findViewById(R.id.btnDirection);
        btnRemove = itemView.findViewById(R.id.btnRemove);


    }




}
