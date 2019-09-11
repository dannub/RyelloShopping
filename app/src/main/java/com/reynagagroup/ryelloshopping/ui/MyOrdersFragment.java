package com.reynagagroup.ryelloshopping.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.MyOrderAdapter;
import com.reynagagroup.ryelloshopping.model.MyOrderItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrderRecycleView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrderRecycleView = view.findViewById(R.id.my_orders_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecycleView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.barang,2,"BAN TERKENAL 45S","Delivered on Mon,15th Jan 2019"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.banner_slide,1,"BAN TERKENAL 45S","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.barang,2,"BAN TERKENAL 45S","Delivered on Mon,15th Jan 2019"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.barang,4,"BAN TERKENAL 45S","Delivered on Mon,15th Jan 2019"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrderRecycleView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }

}
