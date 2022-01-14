package com.example.marketingapplication.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketingapplication.R;
import com.example.marketingapplication.model.StaticRecyclerModel;

import java.util.ArrayList;

public class StaticRecyclerAdapter extends RecyclerView.Adapter<StaticRecyclerAdapter.StaticRecyclerViewHolder> {

    private ArrayList<StaticRecyclerModel> items;
    int row_index = -1;

    public StaticRecyclerAdapter(ArrayList<StaticRecyclerModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_recycler_categories, parent, false);
        StaticRecyclerViewHolder srvh = new StaticRecyclerViewHolder(view);
        return srvh;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StaticRecyclerModel currentItem = items.get(position);
        holder.IV_CategoriesIcon.setImageResource(currentItem.getImage());
        holder.TV_CategoriesName.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if(row_index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.static_recycler_bg);
        }
        else{
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StaticRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView IV_CategoriesIcon;
        TextView TV_CategoriesName;
        LinearLayout linearLayout;

        public StaticRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            IV_CategoriesIcon = itemView.findViewById(R.id.IV_CategoriesIcon);
            TV_CategoriesName = itemView.findViewById(R.id.TV_CategoriesName);
            linearLayout = itemView.findViewById(R.id.LL_Categories);
        }
    }
}
