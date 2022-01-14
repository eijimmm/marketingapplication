package com.example.marketingapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketingapplication.FilterProduct;
import com.example.marketingapplication.ProductViewActivity;
import com.example.marketingapplication.R;
import com.example.marketingapplication.ShopDetailsActivity;
import com.example.marketingapplication.model.ProductModel;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {

    private Context context;
    public ArrayList<ProductModel> productList, filterList;
    private FilterProduct filter;
//    private int position;

    public ProductAdapter(Context context, ArrayList<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        //get data
        ProductModel productModel = productList.get(position);
        String id = productModel.getProductId();
        String uid = productModel.getUid();
        String productName = productModel.getProductName();
        String discountAvailable = productModel.getDiscountAvailable();
        String discountNote = productModel.getDiscountNote();
        String discountPrice = productModel.getDiscountPrice();
        String originalPrice = productModel.getOriginalPrice();
        String productSize = productModel.getProductSize();
        String productCategory = productModel.getProductCategory();
        String productDescription = productModel.getProductDescription();
        String image = productModel.getProductImage();
        String productStock = productModel.getProductStock();
        String timestamp = productModel.getTimestamp();

        //set data
        holder.TV_ModelProdName.setText(productName);
        holder.TV_DiscountNote.setText(discountNote);
        holder.TV_ModelDiscountedPrice.setText(discountPrice);
        holder.TV_ModelOriginalPrice.setText(originalPrice);

        if(discountAvailable.equals("true")){
            //product is on discount
            holder.TV_ModelDiscountedPrice.setVisibility(View.VISIBLE);
            holder.TV_DiscountNote.setVisibility(View.VISIBLE);
            holder.TV_ModelOriginalPrice.setPaintFlags(holder.TV_ModelOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            //product is not on discount
            holder.TV_ModelDiscountedPrice.setVisibility(View.GONE);
            holder.TV_DiscountNote.setVisibility(View.GONE);
        }

        try{
            Picasso.get().load(image).placeholder(R.drawable.ic_baseline_store_24).into(holder.IV_ModelProdImage);
        }
        catch (Exception e){
            holder.IV_ModelProdImage.setImageResource(R.drawable.ic_baseline_store_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle item clicks, show item details
                Intent intent = new Intent(context, ProductViewActivity.class);
//                intent.putExtra("prodUid", uid);
                intent.putExtra("productId", productModel.getProductId());
                intent.putExtra("productImage", productModel.getProductImage());
                intent.putExtra("productName", productModel.getProductName());
                intent.putExtra("originalPrice", productModel.getOriginalPrice());
                intent.putExtra("productStock" , productModel.getProductStock());
                intent.putExtra("productSize", productModel.getProductSize());
                intent.putExtra("productCategory", productModel.getProductCategory());
                intent.putExtra("productDescription", productModel.getProductDescription());
                intent.putExtra("discountPrice", productModel.getDiscountPrice());
                context.startActivity(intent);
            }
        });

        //Favorite System
        if (productModel.isFavourite){
            holder.CB_Favorite.setChecked(true);
        }
        else{
            holder.CB_Favorite.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }

    class ProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        //holds views of recyclerview
        private ImageView IV_ModelProdImage;
        private TextView TV_ModelProdName, TV_ModelDiscountedPrice, TV_ModelOriginalPrice, TV_DiscountNote;
        private CheckBox CB_Favorite;
        private Button BTN_Add;
        private DatabaseReference DR_Favorite;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            IV_ModelProdImage = itemView.findViewById(R.id.IV_ModelProdImage);
            TV_ModelProdName = itemView.findViewById(R.id.TV_ModelProdName);
            TV_ModelDiscountedPrice = itemView.findViewById(R.id.TV_ModelDiscountedPrice);
            TV_ModelOriginalPrice = itemView.findViewById(R.id.TV_ModelOriginalPrice);
            TV_DiscountNote = itemView.findViewById(R.id.TV_DiscountNote);
            CB_Favorite = itemView.findViewById(R.id.CB_Favorite);

            CB_Favorite.setOnCheckedChangeListener(this);

        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Toast.makeText(context, "Please login first", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                return;
            }

            int position = getAdapterPosition();
            ProductModel productModel = productList.get(position);

            //database reference
            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //parse to unique id
                    .child("Favourites"); // to create node fav

            //b = true; adding to fav
            if(b){
                dbFav.child(productModel.getProductId()).setValue(productModel);
//                Toast.makeText(context, "Saved to favourite", Toast.LENGTH_SHORT).show();
            }
            else{
                dbFav.child(productModel.getProductId()).setValue(null);
//                Toast.makeText(context, "Removed to favourite", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
