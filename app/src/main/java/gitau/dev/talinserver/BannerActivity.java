package gitau.dev.talinserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import gitau.dev.talinserver.Common.Common;
import gitau.dev.talinserver.Models.Banners;
import gitau.dev.talinserver.ViewHolder.BannerViewHolder;
import info.hoang8f.widget.FButton;

public class BannerActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference banners;
    FirebaseStorage storage;
    StorageReference mStorageReference;

    FirebaseRecyclerAdapter<Banners, BannerViewHolder> adapter;

    //Add new Banner
    MaterialEditText edtName,edtFoodId;
    FButton btnUpload,btnSelect;

    Banners newBanner;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        //init Firebase
        db = FirebaseDatabase.getInstance();
        banners = db.getReference("Banner");
        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();


        //Init
        mRecyclerView = findViewById(R.id.banners_recycler);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);


        fab = findViewById(R.id.banners_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBannerDialog();
            }
        });

        loadBannersList();

    }

    private void loadBannersList() {

        FirebaseRecyclerOptions<Banners> allBanners = new FirebaseRecyclerOptions.Builder<Banners>()
                .setQuery(banners,Banners.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Banners, BannerViewHolder>(allBanners) {
            @Override
            protected void onBindViewHolder(@NonNull BannerViewHolder holder, int position, @NonNull Banners model) {
                holder.banner_name.setText(model.getName());
                Picasso.get()
                        .load(model.getImage())
                        .into(holder.banner_image);
            }

            @NonNull
            @Override
            public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.banner_item_layout,viewGroup,false);
                return new BannerViewHolder(itemView);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    private void showAddBannerDialog() {
        AlertDialog.Builder dialog  =new AlertDialog.Builder(this);
        dialog.setTitle("ADD NEW Banner");
        dialog.setMessage("Please Fill in Full details");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_banner = inflater.inflate(R.layout.add_new_banner_layout,null);

        edtFoodId = add_banner.findViewById(R.id.edtfoodid);
        edtName = add_banner.findViewById(R.id.edtFoodName);

        btnSelect = add_banner.findViewById(R.id.btnSelect);
        btnUpload = add_banner.findViewById(R.id.btnUpload);

        //set event for selecting picture from phone
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        //set event for uploading picture
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });

        dialog.setView(add_banner);
        dialog.setIcon(R.drawable.ic_laptop_black_24dp);

        //Set Button for dialog
        dialog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(newBanner != null)
                    banners.push()
                            .setValue(newBanner);

                loadBannersList();

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newBanner = null;

                loadBannersList();

            }
        });

        dialog.show();
    }

    private void UploadImage() {
        if(filePath!= null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String Image_name= UUID.randomUUID().toString();
            final StorageReference imageFolder = mStorageReference.child("images/"+Image_name);
            imageFolder.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new Food if image upload and we can get download link
                                    newBanner = new Banners();
                                    newBanner.setName(edtName.getText().toString());
                                    newBanner.setId(edtFoodId.getText().toString());
                                    newBanner.setImage(filePath.toString());


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploading"+progress+"%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data != null && data.getData()!=null)
        {
            filePath= data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateBannerDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }else if(item.getTitle().equals(Common.DELETE))
        {
            deleteBanner(adapter.getRef(item.getOrder()).getKey());

        }


        return super.onContextItemSelected(item);
    }

    private void showUpdateBannerDialog(final String key, final Banners item) {
        // Copy code from add food function
        AlertDialog.Builder dialog  =new AlertDialog.Builder(this);
        dialog.setTitle("EDIT BANNER");
        dialog.setMessage("Please Fill in Full details");

        LayoutInflater inflater = this.getLayoutInflater();
        View edit_banner = inflater.inflate(R.layout.add_new_banner_layout,null);

        edtName = edit_banner.findViewById(R.id.edtFoodName);
        edtFoodId = edit_banner.findViewById(R.id.edtfoodid);



        //Set default values for view
        edtName.setText(item.getName());
        edtFoodId.setText(item.getId());


        btnSelect = edit_banner.findViewById(R.id.btnSelect);
        btnUpload = edit_banner.findViewById(R.id.btnUpload);

        //Event for button

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(); //Let User select image from gallery and save uri of image to storage
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeImage(item);
            }
        });


        dialog.setView(edit_banner);
        dialog.setIcon(R.drawable.ic_laptop_black_24dp);


        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();



                //Update Information
                item.setName(edtName.getText().toString());
                item.setId(edtFoodId.getText().toString());


                //Make Update
                Map<String,Object> update = new HashMap<>();
                update.put("id",item.getId());
                update.put("name",item.getName());
                update.put("image",item.getImage());



                banners.child(key)
                        .updateChildren(update)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(rootLayout,"Updated",Snackbar.LENGTH_SHORT).show();
                                loadBannersList();
                            }
                        });

                Snackbar.make(rootLayout," Banner "+item.getName()+"Was Created",Snackbar.LENGTH_SHORT).show();


                loadBannersList();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                loadBannersList();
            }
        });
        dialog.show();
    }

    private void ChangeImage(final Banners item) {
        if(filePath!= null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String Image_name = UUID.randomUUID().toString();
            final StorageReference imageFolder = mStorageReference.child("images/" + Image_name);
            imageFolder.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for new Category if image upload and we can get download link
                                    item.setImage(uri.toString());

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploading" + progress + "%");
                        }
                    });
        }
    }

    private void deleteBanner(String key) {
        banners.child(key).removeValue();
    }
}
