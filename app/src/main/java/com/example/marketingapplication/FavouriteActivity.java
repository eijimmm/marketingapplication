package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketingapplication.adapter.ProductAdapter;
import com.example.marketingapplication.adapter.ShopsAdapter;
import com.example.marketingapplication.adapter.ShopsAdapter2;
import com.example.marketingapplication.model.ProductModel;
import com.example.marketingapplication.model.ShopsModel;
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

public class FavouriteActivity extends AppCompatActivity {

    private TextView TV_TabProducts, TV_TabStore;
    private RelativeLayout RL_Product, RL_Store;


    ArrayList<ProductModel> favList;
    ArrayList<ShopsModel> favShopsList;
    RecyclerView RV_Fav, RV_FavStore;
    ProductAdapter productAdapter;
    ShopsAdapter2 shopsAdapter;

    DatabaseReference dbFav;

    //navigation
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        TV_TabProducts = findViewById(R.id.TV_TabProducts);
        TV_TabStore = findViewById(R.id.TV_TabStore);
        RL_Product = findViewById(R.id.RL_Product);
        RL_Store = findViewById(R.id.RL_Store);

        //favorite product
        favList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, favList);

        RV_Fav = findViewById(R.id.RV_Fav);
        RV_Fav.setHasFixedSize(true);
        RV_Fav.setLayoutManager(new GridLayoutManager(this, 2));
        RV_Fav.setAdapter(productAdapter);

        //favorite shops
        favShopsList = new ArrayList<>();
        shopsAdapter = new ShopsAdapter2(this, favShopsList);

        RV_FavStore = findViewById(R.id.RV_FavStore);
        RV_FavStore.setHasFixedSize(true);
        RV_FavStore.setLayoutManager(new LinearLayoutManager(this));
        RV_FavStore.setAdapter(shopsAdapter);

        TV_TabProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load fav products
                showFavProductsUI();
                loadFavStore();
            }
        });

        TV_TabStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load fav stores
                showFavStoreUI();

            }
        });

        loadProducts();

        //navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Nav_favourite);

        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Nav_shoes:
                        startActivity(new Intent(getApplicationContext(), DiscoverActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Nav_home:
                        startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Nav_favourite:
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFavStore() {
        if(favShopsList != null){
            favShopsList.clear();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("FavouritesStore")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ShopsModel shopsModel = ds.getValue(ShopsModel.class);
//                            String shopID = "" + ds.child("uid").getValue();
//
//                            //set id to model
//                            shopsModel.setUid(shopID);

                            shopsModel.isFavourite = true;

                            //add model to list
                            favShopsList.add(shopsModel);
                        }
                        shopsAdapter = new ShopsAdapter2(FavouriteActivity.this, favShopsList);
                        RV_FavStore.setAdapter(shopsAdapter);
//                        shopsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Tak jadi doh");

                    }
                });
    }

    private void loadProducts() {
        if(favList != null){
            favList.clear();
        }

        dbFav = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Favourites");

        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ProductModel productModel = ds.getValue(ProductModel.class);

                    productModel.isFavourite = true;
                    favList.add(productModel);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showFavProductsUI() {
        //show prod hide store
        RL_Product.setVisibility(View.VISIBLE);
        RL_Store.setVisibility(View.GONE);

        TV_TabProducts.setTextColor(getResources().getColor(R.color.white));
        TV_TabProducts.setBackgroundResource(R.drawable.shape_green);

        TV_TabStore.setTextColor(getResources().getColor(R.color.green));
        TV_TabStore.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void showFavStoreUI() {
        //show store hide prod
        RL_Store.setVisibility(View.VISIBLE);
        RL_Product.setVisibility(View.GONE);

        TV_TabStore.setTextColor(getResources().getColor(R.color.white));
        TV_TabStore.setBackgroundResource(R.drawable.shape_green);

        TV_TabProducts.setTextColor(getResources().getColor(R.color.green));
        TV_TabProducts.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }
}