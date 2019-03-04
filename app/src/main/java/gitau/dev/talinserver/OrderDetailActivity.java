package gitau.dev.talinserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import gitau.dev.talinserver.Common.Common;
import gitau.dev.talinserver.ViewHolder.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    TextView order_id,order_phone,order_address,order_total,order_coment;
    String order_id_value="";
    RecyclerView foodsList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = findViewById(R.id.order_id);
        order_phone = findViewById(R.id.order_phone);
        order_address = findViewById(R.id.order_ship_to);
        order_total = findViewById(R.id.order_total);
        order_coment = findViewById(R.id.order_comment);


        foodsList = findViewById(R.id.listFoods);
        foodsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        foodsList.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            order_id_value = getIntent().getStringExtra("OrderId");

        //Set Values
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getAddress());
        order_coment.setText(Common.currentRequest.getComments());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        foodsList.setAdapter(adapter);
    }
}
