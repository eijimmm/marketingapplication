package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketingapplication.adapter.ShopsAdapter;
import com.example.marketingapplication.adapter.StaticRecyclerAdapter;
import com.example.marketingapplication.model.ShopsModel;
import com.example.marketingapplication.model.StaticRecyclerModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView nameTV, phoneTV, viewAllTV;
    private ImageButton profileIB;

    //shops
    private ViewPager2 VP_Store;
    private ArrayList<ShopsModel> shopsList;
    private ShopsAdapter adapterShop;

    //navigation
    private BottomNavigationView bottomNavigationView;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        nameTV = findViewById(R.id.TV_Hello);
        viewAllTV = findViewById(R.id.TV_ViewAll);
        profileIB = findViewById(R.id.IB_Profile);
        VP_Store = findViewById(R.id.VP_StoreShoes);

        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();

        profileIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open profile activity
                startActivity(new Intent(UserDashboardActivity.this, ProfileCustomerActivity.class));
            }
        });

        viewAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open list store activity
                startActivity(new Intent(UserDashboardActivity.this, ListStoreActivity.class));
            }
        });

        shopsList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        //clear list before adding
                        shopsList.clear();
                        for(DataSnapshot ds: datasnapshot.getChildren()){
                            ShopsModel modelShops = ds.getValue(ShopsModel.class);
                            shopsList.add(modelShops);
                        }
                        //setup adapter
                        adapterShop = new ShopsAdapter(UserDashboardActivity.this, shopsList);
                        //set adapter to recycler view
                        VP_Store.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Nav_home);

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

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.orderByChild("accountType").equalTo("Customer")
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String name = "Hello, " + "" + ds.child("full name").getValue();
                            String profileImage = "" +ds.child("profileImage").getValue();

                            nameTV.setText(name);

                            try{
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_baseline_person_24).into(profileIB);
                            }
                            catch (Exception e){
                                profileIB.setImageResource(R.drawable.ic_baseline_person_24);
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}