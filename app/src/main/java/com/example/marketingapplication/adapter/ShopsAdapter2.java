package com.example.marketingapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketingapplication.FilterStore;
import com.example.marketingapplication.R;
import com.example.marketingapplication.ShopDetailsActivity;
import com.example.marketingapplication.model.ShopsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopsAdapter2 extends RecyclerView.Adapter<ShopsAdapter2.HolderShop> implements Filterable {

    private Context context;
    public ArrayList<ShopsModel> shopsList, filterList;
    private FilterStore filter;

    //constructor
    public ShopsAdapter2(Context context, ArrayList<ShopsModel> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
        this.filterList = shopsList;
    }

    @NonNull
    @Override
    public HolderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_shop.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_shop, parent, false);
        return new HolderShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderShop holder, int position) {
        //get data
        ShopsModel modelShops = shopsList.get(position);
        String accountType = modelShops.getAccountType();
        String email = modelShops.getEmail();
        String storeName = modelShops.getStorename();
        String phone = modelShops.getPhone();
        final String uid = modelShops.getUid();
        String timestamp = modelShops.getTimestamp();
        String profileImage = modelShops.getProfileImage();

        //set data
        holder.TV_ShopName.setText(storeName);
        holder.TV_ShopPhone.setText(phone);

        try{
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_baseline_store_24).into(holder.IV_ShopImage);
        }
        catch (Exception e){
            holder.IV_ShopImage.setImageResource(R.drawable.ic_baseline_store_24);

        }

        //handle click listener, show shop details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetailsActivity.class);
                intent.putExtra("shopUid", uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //return number of record
        return shopsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterStore(this, filterList);
        }
        return filter;
    }

    public class HolderShop extends RecyclerView.ViewHolder{
        //ui views of row_shop.xml
        ImageView IV_ShopImage;
        TextView TV_ShopName, TV_ShopPhone, TV_ShopAddress;
        RatingBar RB_Shop;
        ImageButton IB_Fav;
        DatabaseReference favouriteref;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        public HolderShop(@NonNull View itemView) {
            super(itemView);

            //init uid views
            IV_ShopImage = itemView.findViewById(R.id.IV_ShopImage);
            TV_ShopName= itemView.findViewById(R.id.TV_ShopName);
            TV_ShopPhone = itemView.findViewById(R.id.TV_ShopPhone);
        }
    }
}
