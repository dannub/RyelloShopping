package com.reynagagroup.ryelloshopping.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.RewardModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class  MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean userMiniLayout = false;
    private RecyclerView couponRecyclerView;
    private String productOriginalPrice;
    private TextView selectedcouponTitle;
    private TextView selectedcouponExpiryDate;
    private TextView selectedcouponBody;
    private TextView selectedcouponDiscount;
    private TextView tv_SK;
    private LinearLayout selectedCoupon;
    private LinearLayout selecteddiscountLayout;
    private ConstraintLayout couponLayout;
    private TextView discountedPrice;


    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean userMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.userMiniLayout = userMiniLayout;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean userMiniLayout, RecyclerView couponRecycleView, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView couponDiscount, LinearLayout discountLayout, TextView tv_SK, ConstraintLayout couponLayout, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.userMiniLayout = userMiniLayout;
        this.couponRecyclerView = couponRecycleView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedcouponTitle = couponTitle;
        this.selectedcouponExpiryDate = couponExpiryDate;
        this.selectedcouponBody = couponBody;
        this.selectedcouponDiscount = couponDiscount;
        this.discountedPrice = discountedPrice;
        this.tv_SK = tv_SK;
        this.couponLayout = couponLayout;
        this.selecteddiscountLayout = discountLayout;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (userMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout,viewGroup,false);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout,viewGroup,false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String type = rewardModelList.get(position).getType();
        Timestamp date = rewardModelList.get(position).getValidity();
        String body = rewardModelList.get(position).getCouponBody();
        String discount = rewardModelList.get(position).getDiscount();
        String lowerlimit = rewardModelList.get(position).getLowerLimit();
        String upperlimit = rewardModelList.get(position).getUpperLimit();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        viewholder.SetData(type,date,discount,body,upperlimit,lowerlimit,alreadyUsed);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView couponTitle;
        private TextView couponDiscount;
        private TextView couponExpiryDate;
        private TextView couponBody;
        private LinearLayout discountlayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.reward_title);
            couponDiscount = itemView.findViewById(R.id.discount_reward);
            couponExpiryDate = itemView.findViewById(R.id.reward_till_date);
            couponBody = itemView.findViewById(R.id.reward_body1);
            discountlayout = itemView.findViewById(R.id.discount_layout);

        }

        private void SetData(final String type, final Timestamp date, final String discount, final String body, final String upperLimit, final String lowerLimit,final Boolean alreadyUsed){
            if (type.toUpperCase().equals("DISCOUNT")){
                discountlayout.setVisibility(View.VISIBLE);
                couponTitle.setText(type.toUpperCase());
                couponDiscount.setText(discount+"%");
            }else {
                discountlayout.setVisibility(View.GONE);
                couponTitle.setText("POTONGAN Rp."+discount+"/-");
                couponDiscount.setText("Rp."+discount);
            }


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY");

            if (alreadyUsed){
                couponLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.border_background));
                couponExpiryDate.setText("Already used");
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent4));
            }else {
                couponLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                couponExpiryDate.setText("till "+simpleDateFormat.format(date.toDate()));
            }
            couponBody.setText(body);


            if (userMiniLayout){
                tv_SK.setVisibility(View.VISIBLE);
                selectedcouponExpiryDate.setVisibility(View.VISIBLE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!alreadyUsed){
                            if (type.toUpperCase().equals("DISCOUNT")) {
                                selecteddiscountLayout.setVisibility(View.VISIBLE);
                                selectedcouponTitle.setText(type.toUpperCase());
                                selectedcouponDiscount.setText(discount + "%");
                            } else {
                                selecteddiscountLayout.setVisibility(View.GONE);
                                selectedcouponTitle.setText("POTONGAN Rp." + discount + "/-");
                                selectedcouponDiscount.setText("Rp." + discount);
                            }

                            selectedcouponExpiryDate.setText(simpleDateFormat.format(date.toDate()));
                            selectedcouponBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.toUpperCase().equals("DISCOUNT")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discount) / 100;
                                    discountedPrice.setText("Rp." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rp." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discount)) + "/-");
                                }
                            } else {
                                discountedPrice.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Maaf, kupon ini tidak bisa digunakan di produk ini", Toast.LENGTH_LONG).show();
                            }

                            if (couponRecyclerView.getVisibility() == View.GONE) {
                                couponRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupon.setVisibility(View.GONE);
                            } else {
                                couponRecyclerView.setVisibility(View.GONE);
                                selectedCoupon.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Toast.makeText(itemView.getContext(), "Maaf, kupon ini sudah digunakan", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}
