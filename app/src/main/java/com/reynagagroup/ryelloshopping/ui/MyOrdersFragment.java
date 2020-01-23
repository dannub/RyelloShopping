package com.reynagagroup.ryelloshopping.ui;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reynagagroup.ryelloshopping.DBqueries;
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

    public static RecyclerView myOrderRecycleView;
    public static Context context;

    public static Dialog loadingDialog;
    public static MyOrderAdapter myOrderAdapter;
    public static LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        //loading dialog

        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrderRecycleView = view.findViewById(R.id.my_orders_recyclerview);

        layoutManager = new LinearLayoutManager(getContext());

        context = getContext();

        DBqueries.loadOrders(getContext(),loadingDialog,layoutManager,myOrderRecycleView);


        return view;
    }

}
