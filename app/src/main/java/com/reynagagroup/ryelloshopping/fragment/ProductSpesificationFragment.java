package com.reynagagroup.ryelloshopping.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.ProductSpesificationAdapter;
import com.reynagagroup.ryelloshopping.model.ProductSpesificationModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductSpesificationFragment extends Fragment {


    public ProductSpesificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView productSpesificationRecyclerView;

    public List<ProductSpesificationModel> productSpesificationModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_product_spesification, container, false);

        productSpesificationRecyclerView = view.findViewById(R.id.product_spesification_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productSpesificationRecyclerView.setLayoutManager(linearLayoutManager);


//        productSpesificationModelList.add(new ProductSpesificationModel(0,"General"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//
//        productSpesificationModelList.add(new ProductSpesificationModel(0,"Display"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//
//        productSpesificationModelList.add(new ProductSpesificationModel(0,"General"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//
//        productSpesificationModelList.add(new ProductSpesificationModel(0,"General"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));
//        productSpesificationModelList.add(new ProductSpesificationModel(1,"Diameter","2 m"));

        ProductSpesificationAdapter productSpesificationAdapter = new ProductSpesificationAdapter(productSpesificationModelList);
        productSpesificationRecyclerView.setAdapter(productSpesificationAdapter);
        productSpesificationAdapter.notifyDataSetChanged();

        return view;
    }

}
