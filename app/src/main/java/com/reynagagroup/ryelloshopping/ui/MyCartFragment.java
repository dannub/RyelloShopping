package com.reynagagroup.ryelloshopping.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.reynagagroup.ryelloshopping.AddAddressActivity;
import com.reynagagroup.ryelloshopping.DeliveryActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.model.CartItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView = view.findViewById(R.id.cart_item_recycleview);
        continueBtn = view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",2,"Rp. 499999/-","Rp.500000/-",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",0,"Rp. 499999/-","Rp.500000/-",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",2,"Rp. 499999/-","Rp.500000/-",1,2,0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rp.1699999/-","Free","Rp 3000/-","Rp.1699999/-"));


        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(getContext(), AddAddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });
        return  view;
    }

}
