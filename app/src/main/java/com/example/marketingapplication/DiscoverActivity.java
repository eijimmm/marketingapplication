package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketingapplication.adapter.ProductAdapter;
import com.example.marketingapplication.model.ProductModel;
import com.example.marketingapplication.model.ShopsModel;
import com.example.marketingapplication.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverActivity extends AppCompatActivity {


    private EditText ET_Search;
    private ImageButton BTN_FilterProduct, IB_Favorite;
    private TextView TV_FilteredProducts;
    private RecyclerView RV_Products;
    private String shopUid;

    //navigation
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference databaseUsers, dbFav ;
    private FirebaseAuth firebaseAuth;
    boolean favChecker = false;
    private ProductModel products;

    private ArrayList<ProductModel> productList;
    private ArrayList<ProductModel> favList;
    private ProductAdapter productAdapter;
    private String userID, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        //init values
        ET_Search = findViewById(R.id.ET_Search);
        BTN_FilterProduct = findViewById(R.id.BTN_FilterProduct);
        TV_FilteredProducts = findViewById(R.id.TV_FilteredProducts);
        RV_Products = findViewById(R.id.RV_Products);
        favList = new ArrayList<>();

        productAdapter = new ProductAdapter(this, productList);

        //intent
        Intent intent = getIntent();

        RV_Products.setLayoutManager(new GridLayoutManager(this,2));

        //userID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            dbFav = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Favourites");
            fetchFavProducts();
        }else{
            loadAllProducts();
        }

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

        BTN_FilterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiscoverActivity.this);
                builder.setTitle("Choose Category")
                        .setItems(Constant.productCategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constant.productCategories1[which];
                                TV_FilteredProducts.setText(selected);
                                if (selected.equals("All")){
                                    //load all
                                    loadAllProducts();
                                }
                                else{
                                    //load filtered
                                    loadFilterProducts(selected);
                                }
                            }
                        })
                .show();
            }
        });

        //navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Nav_shoes);

        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Nav_shoes:
                        return true;
                    case R.id.Nav_home:
                        startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Nav_favourite:
                        startActivity(new Intent(getApplicationContext(), FavouriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFilterProducts(String selected) {
        productList = new ArrayList<>();

        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        productList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            for(DataSnapshot ds2: ds.child("Products").getChildren()){

                                String productCategory = ""+ds2.child("productCategory").getValue();

                                //if selected category matches product category then add in list
                                if (selected.equals(productCategory)){
                                    ProductModel productModel = ds2.getValue(ProductModel.class);
                                    if(isFavChecker(productModel)){
                                        productModel.isFavourite = true;
                                    }
                                    productList.add(productModel);
                                }
                            }
                        }
                        //setup adapter
                        productAdapter = new ProductAdapter(DiscoverActivity.this, productList);
                        //set adapter
                        RV_Products.setAdapter(productAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllProducts() {
        productList = new ArrayList<>();

        //get all products
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //before getting reset list
                        productList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            for(DataSnapshot ds2: ds.child("Products").getChildren()){
                                ProductModel productModel = ds2.getValue(ProductModel.class);
                                if(isFavChecker(productModel)){
                                    productModel.isFavourite = true;
                                }
                                productList.add(productModel);
                            }
                        }
                        //setup adapter
                        productAdapter = new ProductAdapter(DiscoverActivity.this, productList);
                        //set adapter
                        RV_Products.setAdapter(productAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    //fetch all fav product of that particular category
    private void fetchFavProducts(){
        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            ProductModel productModel = productSnapshot.getValue(ProductModel.class);
                            favList.add(productModel);
                    }
                }
                loadAllProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isFavChecker(ProductModel products){
        for(ProductModel p: favList){
            if(p.getProductId().equals(products.getProductId())){
                return true;
            }
        }
        return false;
    }
}