package com.example.marketingapplication;

import android.widget.Filter;

import com.example.marketingapplication.adapter.ShopsAdapter;
import com.example.marketingapplication.adapter.ShopsAdapter2;
import com.example.marketingapplication.model.ShopsModel;

import java.util.ArrayList;

public class FilterStore extends Filter {

    private ShopsAdapter2 adapter;
    private ArrayList<ShopsModel> filterList;

    public FilterStore(ShopsAdapter2 adapter, ArrayList<ShopsModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if (constraint != null && constraint.length() > 0){
            //search filled not empty, searching something, perform search

            //change to upper case, to make case insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ShopsModel> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++){
                //check, search by title and category
                if (filterList.get(i).getStorename().toUpperCase().contains(constraint)){
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else{
            //search filled not empty, not searching, return original/all/complete list

            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        adapter.shopsList = (ArrayList<ShopsModel>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
