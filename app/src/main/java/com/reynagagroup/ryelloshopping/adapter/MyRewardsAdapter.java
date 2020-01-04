package com.reynagagroup.ryelloshopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean userMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.userMiniLayout = userMiniLayout;
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
        viewholder.SetData(type,date,discount,body,upperlimit,lowerlimit);

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

        private void SetData(final String type, final Timestamp date, final String discount, final String body,String upperLimit,String lowerLimit){
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
            couponExpiryDate.setText("till "+simpleDateFormat.format(date.toDate()));

            couponBody.setText(body);

            if (userMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailActivity.coupontitle.setText(type);
                        ProductDetailActivity.couponexpiryDate.setText(simpleDateFormat.format(date));
                        ProductDetailActivity.couponCouponBody1.setText(body);
                        ProductDetailActivity.coupondiscount.setText(discount);
                        ProductDetailActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
