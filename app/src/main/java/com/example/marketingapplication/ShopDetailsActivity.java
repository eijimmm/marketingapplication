package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.marketingapplication.adapter.ProductAdapter;
import com.example.marketingapplication.model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopDetailsActivity extends AppCompatActivity {

    //declare ui views
    private ImageButton IB_FilterProduct;
    private Button BTN_Fav;
    private ImageView IV_ShopImage, IB_Undo, IB_Menu;
    private TextView TV_Welcome, TV_ShopPhone, TV_Email, TV_FilteredProducts;
    private EditText ET_Search;
    private RecyclerView RV_Products;

    private TextView TV_TabProducts, TV_TabGallery;
    private ConstraintLayout CL_Product, CL_Gallery;
    private ImageSlider imageSlider;

    private String shopUid, shopName, shopEmail, shopPhone, shopImage;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ProductModel> productsList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        //init ui views
        IB_Undo = findViewById(R.id.IB_Undo);
        IB_Menu = findViewById(R.id.IB_Menu);
        
        BTN_Fav = findViewById(R.id.BTN_Fav);
        IB_FilterProduct = findViewById(R.id.BTN_FilterProduct);
        
        IV_ShopImage = findViewById(R.id.IV_ShopImage);
        TV_Welcome = findViewById(R.id.TV_Welcome);
        TV_ShopPhone = findViewById(R.id.TV_ShopPhone);
//        TV_Email = findViewById(R.id.TV_Email);
        TV_FilteredProducts = findViewById(R.id.TV_FilteredProducts);
        ET_Search = findViewById(R.id.ET_Search);
        RV_Products = findViewById(R.id.RV_Products);

        imageSlider = findViewById(R.id.image_slider);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        
        BTN_Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoFavourite();
            }
        });

        loadShopGallery();
        loadShopDetails();
        loadShopProducts();

        firebaseAuth = FirebaseAuth.getInstance();
        RV_Products.setLayoutManager(new GridLayoutManager(this,2));


        //search
        ET_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try{
                    productAdapter.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        IB_Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go previous activity
                onBackPressed();
            }
        });

        IB_FilterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsActivity.this);
                builder.setTitle("Choose Category")
                        .setItems(Constant.productCategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constant.productCategories1[which];
                                TV_FilteredProducts.setText(selected);
                                if (selected.equals("All")){
                                    //load all
                                    loadShopProducts();
                                }
                                else{
                                    //load filtered
                                    productAdapter.getFilter().filter(selected);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void addtoFavourite() {
        long timestamp = System.currentTimeMillis();

        //setup data to add in firebase db of current user for favorite store
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid","" + shopUid);
        hashMap.put("name","" + shopName);
        hashMap.put("email", "" + shopEmail);
        hashMap.put("phone", "" + shopPhone);
        hashMap.put("profileImage","" + shopImage);
        hashMap.put("timestamp", "" + timestamp);

        //save to db
        DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("Users");
        dbFav.child(firebaseAuth.getUid()).child("FavoritesStore").child(shopUid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Added to your favorite list", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add to favorite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadShopGallery() {
        final List<SlideModel> list = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Gallery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    list.add(new SlideModel(ds.child("pictureImage").getValue().toString(), ds.child("pictureCaption").getValue().toString(), ScaleTypes.FIT));
                    imageSlider.setImageList(list, ScaleTypes.FIT);

                    imageSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            startActivity(new Intent(ShopDetailsActivity.this, GalleryActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void showProductsUI() {
//        //show prod hide store
//        CL_Product.setVisibility(View.VISIBLE);
//        CL_Gallery.setVisibility(View.GONE);
//
//        TV_TabProducts.setTextColor(getResources().getColor(R.color.white));
//        TV_TabProducts.setBackgroundResource(R.drawable.shape_red);
//
//        TV_TabGallery.setTextColor(getResources().getColor(R.color.green));
//        TV_TabGallery.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//    }
//
//    private void showGalleryUI() {
//        //show prod hide store
//        CL_Gallery.setVisibility(View.VISIBLE);
//        CL_Product.setVisibility(View.GONE);
//
//        TV_TabGallery.setTextColor(getResources().getColor(R.color.white));
//        TV_TabGallery.setBackgroundResource(R.drawable.shape_red);
//
//        TV_TabProducts.setTextColor(getResources().getColor(R.color.green));
//        TV_TabProducts.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.m_share){
            Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.m_back){
            startActivity(new Intent(ShopDetailsActivity.this, UserDashboardActivity.class));
            finish();
        }else if(id == R.id.m_help){
            startActivity(new Intent(ShopDetailsActivity.this, HelpActivity.class));
            finish();
        }
        return true;
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get shop data
                String name = ""+dataSnapshot.child("name").getValue();
                shopName = ""+dataSnapshot.child("storename").getValue();
                shopPhone = ""+dataSnapshot.child("phone").getValue();
                shopEmail = ""+dataSnapshot.child("email").getValue();
                shopImage = ""+dataSnapshot.child("profileImage").getValue();

                //set data
                TV_Welcome.setText(shopName);
                TV_ShopPhone.setText(shopPhone);
//                TV_Email.setText(shopEmail);

                try{
                    Picasso.get().load(shopImage).into(IV_ShopImage);
                }
                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadShopProducts() {
        //init list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //clear list before adding items
                        productsList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            ProductModel productModel = ds.getValue(ProductModel.class);
                            productsList.add(productModel);
                        }

                        //setup adapter
                        productAdapter = new ProductAdapter(ShopDetailsActivity.this, productsList);
                        //set adapter
                        RV_Products.setAdapter(productAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}