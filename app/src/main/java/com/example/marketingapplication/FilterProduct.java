package com.example.marketingapplication;

import android.widget.Filter;

import com.example.marketingapplication.adapter.ProductAdapter;
import com.example.marketingapplication.model.ProductModel;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private ProductAdapter adapter;
    private ArrayList<ProductModel> filterList;

    public FilterProduct(ProductAdapter adapter, ArrayList<ProductModel> filterList) {
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
            ArrayList<ProductModel> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++){
                //check, search by title and category
                if (filterList.get(i).getProductName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getProductCategory().toUpperCase().contains(constraint)){
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
        adapter.productList = (ArrayList<ProductModel>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
