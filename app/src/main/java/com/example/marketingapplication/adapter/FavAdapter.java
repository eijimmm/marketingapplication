package com.example.marketingapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketingapplication.FilterProduct;
import com.example.marketingapplication.R;
import com.example.marketingapplication.model.FavoritesList;
import com.example.marketingapplication.model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder> {

    private Context context;
    public ArrayList<FavoritesList> favlist;
    private FilterProduct filter;

    public FavAdapter(Context context, ArrayList<FavoritesList> favList){
        this.context = context;
        this.favlist = favList;
    }
    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new FavAdapter.FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.FavHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return favlist.size();
    }

    public class FavHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        //holds views of recyclerview
        private ImageView IV_ModelProdImage;
        private TextView TV_ModelProdName, TV_ModelDiscountedPrice, TV_ModelOriginalPrice, TV_DiscountNote;
        private CheckBox CB_Favorite;

        public FavHolder(@NonNull View itemView) {
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
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Toast.makeText(context, "Please login first", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                return;
            }

            int position = getAdapterPosition();
            FavoritesList productModel = favlist.get(position);

            //database reference
            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //parse to unique id
                    .child("Favourites"); // to create node fav

            //b = true; adding to fav
            if(b){
                dbFav.child(productModel.getProductId()).setValue(productModel);
            }
            else{
                dbFav.child(productModel.getProductId()).setValue(null);
            }
        }
    }
}
