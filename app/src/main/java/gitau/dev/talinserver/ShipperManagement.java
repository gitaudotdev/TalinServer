package gitau.dev.talinserver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import gitau.dev.talinserver.Common.Common;
import gitau.dev.talinserver.Models.Shipper;
import gitau.dev.talinserver.ViewHolder.ShipperViewHolder;

public class ShipperManagement extends AppCompatActivity {

    FloatingActionButton fabAdd;

    FirebaseDatabase database;
    DatabaseReference shippers;

    public RecyclerView recycler;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Shipper, ShipperViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_management);

        //Init View
        fabAdd = findViewById(R.id.add_fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShipperLayout();
            }
        });

        recycler = findViewById(R.id.shippers_recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        //Firebase
        database = FirebaseDatabase.getInstance();
        shippers= database.getReference(Common.SHIPPERS_TABLE);
        
        //Load all Shippers
        loadAllShippers();
    }

    private void loadAllShippers() {
        FirebaseRecyclerOptions<Shipper> allShippers = new FirebaseRecyclerOptions.Builder<Shipper>()
                .setQuery(shippers,Shipper.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Shipper, ShipperViewHolder>(allShippers) {
            @Override
            protected void onBindViewHolder(@NonNull ShipperViewHolder holder, final int position, @NonNull final Shipper model) {
                    holder.shipper_name.setText(model.getName());
                    holder.shipper_phone.setText(model.getPhone());

                    holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showEditDialog(adapter.getRef(position).getKey(),model);
                        }
                    });

                    holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeShipper(adapter.getRef(position).getKey());
                        }
                    });
            }

            @NonNull
            @Override
            public ShipperViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view  = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.shipper_layout,viewGroup,false);
                return new ShipperViewHolder(view);
            }
        };
        adapter.startListening();
        recycler.setAdapter(adapter);
    }

    private void removeShipper(String key) {
        shippers.child(key)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShipperManagement.this, "Removed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShipperManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showEditDialog(String key,Shipper model) {
        AlertDialog.Builder shipper_dialog = new AlertDialog.Builder(this);
        shipper_dialog.setTitle("Update Shipper");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_shipper_layout,null);

        final MaterialEditText edtName = view.findViewById(R.id.edt_Name);
        final MaterialEditText edtPhone = view.findViewById(R.id.edt_phone);
        final MaterialEditText edtPass = view.findViewById(R.id.edt_pass);

        //Set Data
        edtName.setText(model.getName());
        edtPass.setText(model.getPhone());
        edtPhone.setText(model.getPhone());

        shipper_dialog.setView(view);

        shipper_dialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        shipper_dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


                Map<String,Object> update = new HashMap<>();
                update.put("name",edtName.getText().toString());
                update.put("phone",edtPhone.getText().toString());
                update.put("password",edtPass.getText().toString());

                shippers.child(edtPhone.getText().toString())
                        .updateChildren(update)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShipperManagement.this, "Shipper Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShipperManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        shipper_dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        shipper_dialog.show();

    }

    private void showCreateShipperLayout() {
        AlertDialog.Builder shipper_dialog = new AlertDialog.Builder(this);
        shipper_dialog.setTitle("Create Shipper");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_shipper_layout,null);

        final MaterialEditText edtName = view.findViewById(R.id.edt_Name);
        final MaterialEditText edtPhone = view.findViewById(R.id.edt_phone);
        final MaterialEditText edtPass = view.findViewById(R.id.edt_pass);

        shipper_dialog.setView(view);

        shipper_dialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        shipper_dialog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();


                Shipper shipper = new Shipper();
                shipper.setName(edtName.getText().toString());
                shipper.setPassword(edtPass.getText().toString());
                shipper.setPhone(edtPhone.getText().toString());

                shippers.child(edtPhone.getText().toString())
                        .setValue(shipper)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShipperManagement.this, "Shipper Created", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShipperManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        shipper_dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        shipper_dialog.show();


    }
}
