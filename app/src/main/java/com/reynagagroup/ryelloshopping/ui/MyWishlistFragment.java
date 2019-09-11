package com.reynagagroup.ryelloshopping.ui;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.WishlistAdapter;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;

import static com.reynagagroup.ryelloshopping.DBqueries.loadWishlist;
import static com.reynagagroup.ryelloshopping.DBqueries.wishlist;
import static com.reynagagroup.ryelloshopping.DBqueries.wishlistModelList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishlistFragment extends Fragment {


    public MyWishlistFragment() {
        // Required empty public constructor
    }


    private RecyclerView wistListRecyclerView;
    private Dialog loadingDialog;
    public  static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);


        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        wistListRecyclerView = view.findViewById(R.id.my_wishlist_recyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wistListRecyclerView.setLayoutManager(linearLayoutManager);

        if (wishlistModelList.size()==0){
            wishlist.clear();
            loadWishlist(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }


        wishlistAdapter = new WishlistAdapter(wishlistModelList,true);
        wistListRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();

        return view;
    }

}
