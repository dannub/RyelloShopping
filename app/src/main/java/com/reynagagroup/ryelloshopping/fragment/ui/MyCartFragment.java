package com.reynagagroup.ryelloshopping.fragment.ui;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.DeliveryActivity;
import com.reynagagroup.ryelloshopping.Interface.IOnBackPressed;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.RewardModel;

import java.util.ArrayList;

import static com.reynagagroup.ryelloshopping.DBqueries.cartItemModelList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment implements IOnBackPressed {


    public MyCartFragment() {
        // Required empty public constructor
    }

    public static RecyclerView cartItemsRecyclerView;
    public static Button continueBtn;
    public static Fragment mycartfragment;
    public static Fragment this_fragment;


    public static Dialog loadingDialog;
    public  static CartAdapter cartlistAdapter;
   public static TextView totalAmount;
    public static Parcelable recyclerViewState;
    public static LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        //loading dialog




        cartItemsRecyclerView = view.findViewById(R.id.cart_item_recycleview);
        continueBtn = view.findViewById(R.id.deliv_continue_btn);
        totalAmount = view.findViewById(R.id.total_cart_amount);









//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
//
//        cartlistAdapter.notifyDataSetChanged();



        if (!cartItemModelList.isEmpty()) {
            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!totalAmount.getText().toString().replace(" ","").equals("Rp.-/-")) {
                        cartlistAdapter.notifyDataSetChanged();
                        mycartfragment = MyCartFragment.this;



                        DeliveryActivity.cartItemModelList = new ArrayList<>();
                        PaymentActivity.fromCart = true;


                        for (int x = 0; x < DBqueries.cartItemModelList.size() - 1; x++) {
                            CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                            if (cartItemModel.getInStock()) {
                                DeliveryActivity.cartItemModelList.add(cartItemModel);
                            }
                        }
                        DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                        DeliveryActivity.cartlistAdapter = new CartAdapter(getContext(),cartItemsRecyclerView,null,DeliveryActivity.cartItemModelList,DeliveryActivity.totalAmount,false,loadingDialog);
                        DeliveryActivity.cartlistAdapter.SetAdapter(DeliveryActivity.cartlistAdapter);
                        DeliveryActivity.deliveryRecycleView.setAdapter(DeliveryActivity.cartlistAdapter);
                        DeliveryActivity.cartlistAdapter.notifyDataSetChanged();


                        loadingDialog.show();
                        if (DBqueries.addressModelList.size() == 0) {
                            DBqueries.loadAddresses(getContext(), loadingDialog,true,0);
                        } else {
                            loadingDialog.dismiss();
                            Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                            startActivity(deliveryIntent);
                        }
                    }else {
                        MyCartFragment.mycartfragment = null;
                        Toast.makeText(getContext(),"Stock is Empty!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyCartFragment.mycartfragment = null;
                    Toast.makeText(getContext(),"Cart is Empty!",Toast.LENGTH_SHORT).show();
                }
            });
        }




        return  view;
    }

    @Override
    public void onStart() {

        super.onStart();
        this_fragment = MyCartFragment.this;
        DBqueries.loadCartList(getContext(), loadingDialog,true,new TextView(getContext()),totalAmount,false,null);
        DBqueries.loadRewards(getContext(),loadingDialog,false);


        if (cartItemModelList.size()==0){
            totalAmount.setText("Rp. -/-");

        }else {
            if (cartItemModelList.get(cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
        }










    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (CartItemModel cartItemModel : cartItemModelList){
            if (!TextUtils.isEmpty(cartItemModel.getSelectedCouponId())){
                for (RewardModel rewardModel: DBqueries.rewardModelList){
                    if (rewardModel.getCouponId().equals(cartItemModel.getSelectedCouponId())){
                        rewardModel.setAlreadyUsed(false);
                    }
                }
                cartItemModel.setSelectedCouponId(null);
                if (MyRewardsFragment.myRewardsAdapter != null) {
                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    @Override
    public boolean onBackPressed() {
        mycartfragment = null;
        if (MainActivity.showCart){
            MyCartFragment.this.getActivity().finish();
        }
        return true;
    }
}
