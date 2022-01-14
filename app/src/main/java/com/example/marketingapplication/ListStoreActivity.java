package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.marketingapplication.adapter.ShopsAdapter;
import com.example.marketingapplication.adapter.ShopsAdapter2;
import com.example.marketingapplication.model.ShopsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListStoreActivity extends AppCompatActivity {

    //button
    private ImageButton IB_Undo;

    private EditText ET_Search;

    //shops
    private RecyclerView RV_ShopsList;
    private ArrayList<ShopsModel> shopsList;
    private ShopsAdapter2 adapterShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store);

        IB_Undo = findViewById(R.id.IB_Undo);
        RV_ShopsList = findViewById(R.id.RV_ShopsList);
        ET_Search = findViewById(R.id.ET_Search);

        RV_ShopsList.setLayoutManager(new LinearLayoutManager(this));

        //search
        ET_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try{
                    adapterShop.getFilter().filter(s);
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
                        adapterShop = new ShopsAdapter2(ListStoreActivity.this, shopsList);
                        //set adapter to recycler view
                        RV_ShopsList.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}