package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marketingapplication.adapter.ProductAdapter;
import com.example.marketingapplication.model.ProductModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductViewActivity extends AppCompatActivity {

    private ImageView IV_ImageView;
    private TextView TV_ProductName, TV_ProductPrice, TV_CategoryDetails, TV_DiscountPrice, TV_ProductDetails, TV_ProductSize, TV_ProductStock;
    private ImageButton IB_Undo;

    boolean discountAvailable = false;

    private String prodUid, shopUid;
    private ArrayList<ProductModel> productsList;
    private ProductAdapter productAdapter;

    String mProdId, mProdImage, mProdName, mProdPrice, mProdCategory, mDiscountPrice, mProdDetails, mProdSize, mProdStock;

    Button BTN_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        //init views
        IV_ImageView = findViewById(R.id.IV_ProductImage);
        TV_ProductName = findViewById(R.id.TV_ProductName);
        TV_ProductPrice = findViewById(R.id.TV_ProductPrice);
        TV_CategoryDetails = findViewById(R.id.TV_CategoryDetails);
        TV_DiscountPrice = findViewById(R.id.TV_DiscountPrice);
        TV_ProductDetails = findViewById(R.id.TV_ProductDetails);
        TV_ProductSize = findViewById(R.id.TV_ProductSize);
        TV_ProductStock = findViewById(R.id.TV_ProductStock);


        IB_Undo = findViewById(R.id.IB_Undo);
        IB_Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go previous activity
                onBackPressed();
            }
        });

        //get uid of the product from intent
        prodUid = getIntent().getStringExtra("prodUid");
        shopUid = getIntent().getStringExtra("shopUid");

        loadProductDetails();
    }

    private void loadProductDetails() {
        mProdImage = getIntent().getStringExtra("productImage");
        mProdName = getIntent().getStringExtra("productName");
        mProdCategory = getIntent().getStringExtra("productCategory");
        mProdPrice = getIntent().getStringExtra("originalPrice");
        mDiscountPrice = getIntent().getStringExtra("discountPrice");
        mProdDetails = getIntent().getStringExtra("productDescription");
        mProdSize = getIntent().getStringExtra("productSize");
        mProdStock = getIntent().getStringExtra("productStock");

        Picasso.get().load(mProdImage).into(IV_ImageView);
            TV_ProductName.setText(mProdName);
            TV_ProductPrice.setText(mProdPrice);
            TV_ProductDetails.setText(mProdDetails);
            TV_ProductSize.setText("Available Size: US " + mProdSize);
            TV_ProductStock.setText("Available Stock: " + mProdStock);
            TV_CategoryDetails.setText(mProdCategory);




//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(shopUid).child("Products").child(prodUid)
//                .addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    mProdImage = snapshot.child("productImage").getValue().toString();
//                    mProdName = snapshot.child("productName").getValue().toString();
//                    mProdPrice = snapshot.child("originalPrice").getValue().toString();
//
//                    Picasso.get().load(mProdImage).into(IV_ImageView);
//                    TV_ProductName.setText(mProdName);
//                    TV_ProductPrice.setText(mProdPrice);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}